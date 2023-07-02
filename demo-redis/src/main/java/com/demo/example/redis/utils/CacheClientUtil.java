package com.demo.example.redis.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author liull
 * @date 2022/5/29 22:30
 * @desc
 */
@Component
public class CacheClientUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将任何对象转成json存入redis中，设置有效期
     * @param key
     * @param value
     * @param time
     * @param unit
     * @param <T>
     */
    public <T> void set(String key,T value,Long time,TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(value),time,unit);
    }

    /**
     * 带有逻辑过期的对象存入redis
     * @param key
     * @param value
     * @param time
     * @param unit
     * @param <T>
     */
    public <T> void setWithLogicalExpire(String key,T value,Long time,TimeUnit unit){
        RedisData<T> redisData = new RedisData<>();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }

    /**
     * 使用缓存空值解决缓存穿透进行查询
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R,ID> R queryWithPassThrough(String keyPrefix,
                                             ID id,
                                             Class<R> type,
                                             Function<ID,R> dbFallback,
                                             Long time,
                                             TimeUnit unit){
        //查询redis缓存的信息
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json, type);
        }
        //判断命中的是否是空值，如果是空值则直接返回
        if(json != null){
            return null;
        }
        //缓存信息不存在，需要查询数据库
        R r = dbFallback.apply(id);
        if(r == null){
            //将空值写入redis
            stringRedisTemplate.opsForValue().set(key,"",3L, TimeUnit.MINUTES);
            return null;
        }
        //数据库存在数据，需要写入缓存，设置有效期
        this.set(key,r,time,unit);
        return r;
    }



}
