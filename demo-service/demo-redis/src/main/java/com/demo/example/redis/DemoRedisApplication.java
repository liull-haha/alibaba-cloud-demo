package com.demo.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liull
 * @description
 * @date 2021/6/9
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.demo.example")
public class DemoRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisApplication.class,args);
    }

}
