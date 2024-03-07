package com.example.demorocketmq.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author liull
 * @Date 2024/1/28 00:18
 **/
@Configuration
@Slf4j
public class TestConsumerListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//        log.info("消费到的数据：{}", JSONObject.toJSONString(list));
        for (MessageExt messageExt : list) {
            String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("消费到的数据：{}", msg);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
