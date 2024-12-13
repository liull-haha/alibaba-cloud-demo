package com.demo.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liull
 * @description
 * @date 2021/8/30
 */
@SpringBootApplication
//@EnableDiscoveryClient
@ComponentScan("com.demo.example")
public class DemoSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityApplication.class);
    }
}
