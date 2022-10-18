package com.demo.example.redis.utils;

/**
 * @author liull
 * @date 2022/7/3 22:52
 */
public interface ILock {

    /**
     * 获取锁
     * @param timeoutSec  锁的失效时间
     * @return
     */
    boolean tryLock(long timeoutSec);

    /**
     *  释放锁
     */
    void unlock();
}
