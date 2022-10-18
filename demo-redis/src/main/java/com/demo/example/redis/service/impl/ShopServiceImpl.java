package com.demo.example.redis.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.Shop;
import com.demo.example.redis.mapper.ShopMapper;
import com.demo.example.redis.service.IShopService;
import com.demo.example.redis.utils.CacheClientUtil;
import com.demo.example.redis.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.demo.example.redis.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.demo.example.redis.utils.RedisConstants.CACHE_SHOP_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClientUtil cacheClientUtil;

    /**
     * 获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key){
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10L, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(aBoolean);
    }

    /**
     * 释放锁
     * @param key
     */
    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

    @Override
    public Result queryId(Long id) {
        //缓存穿透解决方案
//        Shop shop = this.queryShopWithPassThrough(id);
        Shop shop = cacheClientUtil.queryWithPassThrough(CACHE_SHOP_KEY,id,Shop.class, this::getById,CACHE_SHOP_TTL,TimeUnit.MINUTES);
        //缓存击穿解决方案-互斥锁
//        Shop shop = this.queryShopWithMutex(id);
        //缓存击穿解决方案-逻辑过期
//        Shop shop = this.queryShopWithLogicalExpire(id);
        if(shop == null){
            return Result.fail("店铺不存在");
        }
        return Result.ok(shop);
    }

    public void saveShop2Redis(Long id,Long expireSeconds){
        Shop shop = this.getById(id);
        RedisData<Shop> redisData = new RedisData<>();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(redisData));
    }

    /**
     * 利用逻辑过期解决缓存击穿
     * 提前在redis中缓存热点数据，且不设置ttl，则去查询缓存，未命中时就是热点数据不需要访问了
     * @param id
     * @return
     */
    public Shop queryShopWithLogicalExpire(Long id){
        //查询redis缓存的商铺信息
        String shopJson = stringRedisTemplate.opsForValue().get(CACHE_SHOP_KEY+id);
        if(StrUtil.isBlank(shopJson)){
            return null;
        }
        //******命中，需要判断逻辑过期时间是否过期
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            //未过期,直接返回
            return shop;
        }
        //******缓存数据已过期，需要重建缓存
        boolean lock = this.tryLock("lock:shop:" + id);
        if(lock){
            //注意获取锁成功后应该再次检测redis缓存数据是否过期，做双重检查过期
            try{
                //******成功获取互斥锁，开启独立线程，实现缓存重建
                this.saveShop2Redis(id,30L);
            }catch (Exception e){
                log.error("",e);
            }finally {
                //******释放锁
                this.unLock("lock:shop:"+id);
            }
        }
        //*******获取锁失败，返回失效的数据
        return shop;
    }

    /**
     * 利用互斥锁解决缓存击穿问题
     * @param id
     * @return
     */
    public Shop queryShopWithMutex(Long id){
        //查询redis缓存的商铺信息
        String shopJson = stringRedisTemplate.opsForValue().get(CACHE_SHOP_KEY+id);
        if(StrUtil.isNotBlank(shopJson)){
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //判断命中的是否是空值，如果是空值则直接返回
        if(shopJson != null){
            return null;
        }
        //****缓存未命中，需要缓存重建
        Shop shop = null;
        try {
            boolean isLock = tryLock("lock:shop:"+id);
            if(!isLock){
                //****获取锁失败，需要休眠后再重试（重试获取缓存数据）
                Thread.sleep(50);
                return queryShopWithMutex(id);
            }
            //****获取锁成功，应该再次检查缓存数据是否存在，如果存在无需重建缓存
            //查询数据库
            shop = this.getById(id);
            if(shop == null){
                // 将空值写入redis
                stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,"",3L, TimeUnit.MINUTES);
                return null;
            }
            //数据库存在数据，需要写入缓存，设置30分钟有效期
            stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(shop),30L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("",e);
        } finally {
            //****释放互斥锁
            unLock("lock:shop:"+id);
        }
        return shop;
    }

    /**
     * 使用缓存空值解决缓存穿透问题
     * 缓存空值，设置有效期，如果查询到空值就不会查询数据库
     * @param id
     * @return
     */
    public Shop queryShopWithPassThrough(Long id){
        //查询redis缓存的商铺信息
        String shopJson = stringRedisTemplate.opsForValue().get(CACHE_SHOP_KEY+id);
        if(StrUtil.isNotBlank(shopJson)){
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //*****判断命中的是否是空值，如果是空值则直接返回
        if(shopJson != null){
            return null;
        }
        //缓存信息不存在，需要查询数据库
        Shop shop = this.getById(id);
        if(shop == null){
            // ******将空值写入redis
            stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,"",3L, TimeUnit.MINUTES);
            return null;
        }
        //数据库存在数据，需要写入缓存，设置30分钟有效期
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(shop),30L, TimeUnit.MINUTES);
        return shop;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateShop(Shop shop) {
        Long id = shop.getId();
        if(id == null){
            return Result.fail("店铺ID不能为空");
        }
        //先更新数据库，再删除缓存
        this.updateById(shop);
        stringRedisTemplate.delete(CACHE_SHOP_KEY+id);
        return Result.ok();
    }
}
