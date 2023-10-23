package com.jef.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tufujie
 * @date 2023/9/7
 */
//@RocketMQMessageListener(topic = "topic_name_jef_test_asyncSend", consumerGroup = "topic_name_jef_test_asyncSend_Group")
@Component
@Slf4j
public class MyConsumerOfAsyncSend implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        // 处理消息的逻辑
        log.info("当前时间：{}收到消息: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()), message);
    }
}