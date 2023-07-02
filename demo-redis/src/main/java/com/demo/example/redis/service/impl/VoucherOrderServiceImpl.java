package com.demo.example.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.SeckillVoucher;
import com.demo.example.redis.entity.VoucherOrder;
import com.demo.example.redis.mapper.VoucherOrderMapper;
import com.demo.example.redis.service.ISeckillVoucherService;
import com.demo.example.redis.service.IVoucherOrderService;
import com.demo.example.redis.utils.RedisIdWorker;
import com.demo.example.redis.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 会出现超卖问题(多线程并发安全问题)
     * @param voucherId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result seckillVoucher(Long voucherId) {
        //查询优惠券数据
        SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucherId);
        //判断秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀未开始");
        }
        //判断秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已结束");
        }
        //判断库存是否充足
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足");
        }
        Long userId = UserHolder.getUser().getId();
        /*//基于Redis分布式锁
        // 获取锁
        SimpleRedisLock lock = new SimpleRedisLock("order:"+userId,stringRedisTemplate);
        boolean tryLock = lock.tryLock(5);
        if(!tryLock){
            //获取锁失败，返回错误或重试
            return Result.fail("不允许重复下单！");
        }
        //获取代理对象（保证事务）
        try {
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            //释放锁 需要判断是否是自己的锁
            lock.unlock();
        }*/

        //基于redisson实现分布式锁
        RLock rLock = redissonClient.getLock("lock:order:" + userId);
        //获取锁失败不重试且不等待，也可以增加参数，设置等待时长进行重试
        boolean tryLock = rLock.tryLock();
        if(!tryLock){
            //获取锁失败，返回错误或重试
            return Result.fail("不允许重复下单！");
        }
        //获取代理对象（保证事务）
        try {
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            //释放锁 需要判断是否是自己的锁
            rLock.unlock();
        }
    }

    @Override
    @Transactional
    public Result createVoucherOrder(Long voucherId){
        //一人一单
        Long userId = UserHolder.getUser().getId();
        Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        if(count>1L){
            return Result.fail("每人限购一张");
        }
        //扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock -1")
                .eq("voucher_id", voucherId)
                .gt("stock",0)
                .update();
        if(!success){
            return Result.fail("库存不足");
        }
        //创建订单，返回订单ID
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        this.save(voucherOrder);
        return Result.ok(orderId);
    }



    private BlockingQueue<VoucherOrder> voucherOrderBlockingQueue = new ArrayBlockingQueue<>(1024*1024);
    private static final ExecutorService seckillOrderExecutor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init(){
        seckillOrderExecutor.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    //获取阻塞队列中订单
                    VoucherOrder voucherOrder = voucherOrderBlockingQueue.take();
                    //处理订单
                    handlerVoucherOrder(voucherOrder);
                } catch (InterruptedException e) {
                    log.error("处理订单异常", e);
                }
            }
        }

        private void handlerVoucherOrder(VoucherOrder voucherOrder) {
            save(voucherOrder);
        }
    }

    @Autowired
    private DefaultRedisScript<Long> seckillScript;
    /**
     * 异步下单
     * @param voucherId
     * @return
     */
    @Override
    public Result seckillVoucherBySync(Long voucherId) {
        //获取用户
        Long userId = UserHolder.getUser().getId();
        //执行lua脚本进行抢购校验
        Long execute = stringRedisTemplate.execute(seckillScript, Collections.emptyList(), voucherId.toString(), userId.toString());
        if(execute == null || execute.intValue() != 0){
            return Result.fail("校验失败");
        }
        //校验通过，将下单信息保存到阻塞队列进行异步下单
        long orderId = redisIdWorker.nextId("order");
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        voucherOrderBlockingQueue.add(voucherOrder);
        return Result.ok(orderId);
    }
}
