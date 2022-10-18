package com.demo.example.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liull
 * @description
 * @date 2021/3/20
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.demo.example")
public class DemoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoProviderApplication.class,args);
    }

}
