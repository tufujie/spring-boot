package com.jef.controller;

import com.jef.entity.User;
import com.jef.service.impl.UserProducerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jef
 * @date 2023/10/30
 */
@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    @Value("${kafka.topic.my-topic}")
    String myTopic;
    @Value("${kafka.topic.my-topic2}")
    String myTopic2;
    private final UserProducerService producer;
    private AtomicInteger atomicLong = new AtomicInteger();

    KafkaController(UserProducerService producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/sendMessageToKafkaTopicByManyPartition")
    public String sendMessageToKafkaTopicByManyPartition(@RequestParam("name") String name) {
        // myTopic有2个分区，当你尝试发送多条消息的时候，你会发现消息会被比较均匀地发送到每个 partion 中。
        for (int i = 0; i < 10; i++) {
            this.producer.sendMessage(myTopic, new User(atomicLong.addAndGet(1), name));
        }
        return "success";
    }

    @GetMapping(value = "/sendMessageToKafkaTopicByOnePartition")
    public String sendMessageToKafkaTopicByOnePartition(@RequestParam("name") String name) {
        // 1个topic一个分区，顺序发送和消费
        for (int i = 0; i < 10; i++) {
            this.producer.sendMessage(myTopic2, new User(atomicLong.addAndGet(1), name));
        }
        return "success";
    }
}