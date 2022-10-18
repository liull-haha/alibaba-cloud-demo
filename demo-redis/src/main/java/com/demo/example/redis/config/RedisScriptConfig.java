package com.demo.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author liull
 * @date 2022/7/16 23:58
 */
@Configuration
public class RedisScriptConfig {

    @Bean
    public DefaultRedisScript<Long> seckillScript(){
        DefaultRedisScript<Long> seckillScript = new DefaultRedisScript<>();
        seckillScript.setLocation(new ClassPathResource("lua/seckill.lua"));
        seckillScript.setResultType(Long.class);
        return seckillScript;
    }
}
