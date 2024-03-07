package com.example.demorocketmq.producer;

//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @Author liull
 * @Date 2024/1/27 23:18
 **/
@RestController
@Slf4j
public class TestProducer {
// rocket-springboot-starter用法
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

//    @GetMapping("/send")
//    public String send(String message){
//
//        rocketMQTemplate.convertAndSend(message);
//
//        return "success";
//    }

    @Autowired
    private DefaultMQProducer producerClient;

    @GetMapping("/send")
    public String send(String message){
        Message msg = new Message("test-topic01",null,message.getBytes(StandardCharsets.UTF_8));
        try {
            SendResult send = producerClient.send(msg);
            log.info("消息发送结果：{}",send.getSendStatus());
        } catch (Exception e) {
            log.error("消息发送失败：",e);
            return "fail";
        }
        return "success";
    }

}