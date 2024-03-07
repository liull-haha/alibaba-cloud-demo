package com.example.demorocketmq.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liull
 * @Date 2024/1/27 23:45
 **/
@Configuration
public class ProducerConfig {


    @Autowired
    private MqConfig mqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer producerClient(){
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(mqConfig.getNameServer());
        producer.setProducerGroup(mqConfig.getProducerGroup());
        producer.setSendMsgTimeout(15000);
        return producer;
    }
}
