package com.demo.example.redis.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author liull
 * @date 2022/7/3 22:54
 */
public class SimpleRedisLock implements ILock{

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true)+"-";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        //加载lua脚本
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("lua/unLock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = KEY_PREFIX+name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识
        String threadId = ID_PREFIX+Thread.currentThread().getId();
        //添加锁时，保存线程标识（唯一性）
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 调用Lua脚本实现判断和删除原子性
     */
    @Override
    public void unlock() {
        //获取线程标识
        String threadId = ID_PREFIX+Thread.currentThread().getId();
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(name),threadId);
    }

    /**
     * 非原子性
     */
    /*@Override
    public void unlock() {
        //获取线程标识
        String threadId = IDPREFIX+Thread.currentThread().getId();
        //删除锁时，判断是否与当前线程标识一致
        String id = stringRedisTemplate.opsForValue().get(name);
        if(threadId.equals(id)) {
            stringRedisTemplate.delete(name);
        }
    }*/
}
