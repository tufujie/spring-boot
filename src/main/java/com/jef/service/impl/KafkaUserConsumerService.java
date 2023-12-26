package com.jef.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.entity.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Jef
 * @date 2023/10/30
 */
@Service
public class KafkaUserConsumerService {
    private final Logger logger = LoggerFactory.getLogger(KafkaUserProducerService.class);

    @Value("${kafka.topic.my-topic}")
    private String myTopic;
    @Value("${kafka.topic.my-topic2}")
    private String myTopic2;
    private final ObjectMapper objectMapper = new ObjectMapper();


    // autoStartup = "false"，是否自动启动,如果是 false,默认不生效,需要手动唤醒。
    @KafkaListener(topics = {"${kafka.topic.my-topic}"}, groupId = "group1", id = "myListener1")
    public void consumeMessage(ConsumerRecord<String, String> userConsumerRecord) {
        try {
            User user = objectMapper.readValue(userConsumerRecord.value(), User.class);
            logger.info("消费者1号消费topic:{} partition:{}的消息 -> {}", userConsumerRecord.topic(),
                    userConsumerRecord.partition(), user.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = {"${kafka.topic.my-topic}"}, groupId = "group1", id = "myListener1.1")
    public void consumeMessage1(ConsumerRecord<String, String> userConsumerRecord) {
        try {
            User user = objectMapper.readValue(userConsumerRecord.value(), User.class);
            logger.info("消费者1号2消费topic:{} partition:{}的消息 -> {}", userConsumerRecord.topic(),
                    userConsumerRecord.partition(), user.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 一个topic一个分区可以很少的保证有序性
    @KafkaListener(topics = {"${kafka.topic.my-topic2}"}, groupId = "group2", id = "myListener2")
    public void consumeMessage2(User user) {
        logger.info("消费者2号消费{}的消息 -> {}", myTopic2, user.toString());
    }
}