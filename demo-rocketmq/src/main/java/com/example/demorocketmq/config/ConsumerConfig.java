package com.example.demorocketmq.config;

import com.example.demorocketmq.consumer.TestConsumerListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liull
 * @Date 2024/1/28 00:07
 **/
@Configuration
public class ConsumerConfig {

    @Autowired
    private MqConfig mqConfig;

    @Autowired
    private TestConsumerListener testConsumerListener;

    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQPushConsumer consumerClient(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(mqConfig.getNameServer());
        consumer.setConsumerGroup(mqConfig.getConsumerGroup());

        try {
            consumer.subscribe("test-topic01","*");
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        consumer.setMessageListener(testConsumerListener);
        return consumer;
    }

}
