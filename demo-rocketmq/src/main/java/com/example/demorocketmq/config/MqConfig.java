package com.example.demorocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liull
 * @Date 2024/1/27 23:55
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MqConfig {


    private String nameServer;

    private String producerGroup;

    private String consumerGroup;



}
