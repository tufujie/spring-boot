package com.jef.service.impl;

import com.jef.entity.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class UserConsumerService {
    private final Logger logger = LoggerFactory.getLogger(UserProducerService.class);

    @Value("${kafka.topic.my-topic}")
    private String myTopic;
    @Value("${kafka.topic.my-topic2}")
    private String myTopic2;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = {"${kafka.topic.my-topic}"}, groupId = "group1")
    public void consumeMessage(ConsumerRecord<String, String> userConsumerRecord) {
        try {
            User book = objectMapper.readValue(userConsumerRecord.value(), User.class);
            logger.info("消费者消费topic:{} partition:{}的消息 -> {}", userConsumerRecord.topic(), userConsumerRecord.partition(), book.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = {"${kafka.topic.my-topic2}"}, groupId = "group2")
    public void consumeMessage2(User user) {
        logger.info("消费者消费{}的消息 -> {}", myTopic2, user.toString());
    }
}