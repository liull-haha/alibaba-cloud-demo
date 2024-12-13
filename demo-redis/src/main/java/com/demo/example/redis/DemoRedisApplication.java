package com.demo.example.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liull
 * @description
 * @date 2021/6/9
 */
@SpringBootApplication
@ComponentScan("com.demo.example")
@MapperScan("com.demo.example.redis.mapper")
public class DemoRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisApplication.class,args);
    }

}
