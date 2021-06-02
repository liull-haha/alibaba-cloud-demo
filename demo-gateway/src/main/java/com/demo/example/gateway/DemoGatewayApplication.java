package com.demo.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liull
 * @description
 * @date 2021/6/1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DemoGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoGatewayApplication.class,args);
    }
}
