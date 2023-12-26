package com.jef.controller;

import com.alibaba.fastjson.JSONObject;
import com.jef.entity.User;
import com.jef.service.impl.KafkaUserProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private final KafkaUserProducerService producer;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    KafkaController(KafkaUserProducerService producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/sendMessageToKafkaTopicByManyPartition")
    public String sendMessageToKafkaTopicByManyPartition(@RequestParam("name") String name) {
        // myTopic有2个分区，当你尝试发送多条消息的时候，你会发现消息会被比较均匀地发送到每个 partion 中。
        for (int i = 0; i < 10; i++) {
            this.producer.sendMessage(myTopic, JSONObject.toJSONString(new User(i, name)));
        }
        return "success";
    }

    @GetMapping(value = "/sendMessageToKafkaTopicByOnePartition")
    public String sendMessageToKafkaTopicByOnePartition(@RequestParam("name") String name) {
        // 1个topic一个分区，顺序发送和消费
        for (int i = 0; i < 10; i++) {
            this.producer.sendMessage(myTopic2, JSONObject.toJSONString(new User(i, name)));
        }
        return "success";
    }

    /**
     * 开启监听
     */
    @GetMapping("/start")
    public void start(@RequestParam String id) {
        // 判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer(id).isRunning()) {
            registry.getListenerContainer(id).start();
        }
        // 将其恢复
        registry.getListenerContainer(id).resume();
    }

    /**
     * 关闭监听
     */
    @GetMapping("/stop")
    public void stop(@RequestParam String id) {
        // 暂停监听
//        registry.getListenerContainer(id).setAutoStartup(false);
        registry.getListenerContainer(id).stop();
    }
}