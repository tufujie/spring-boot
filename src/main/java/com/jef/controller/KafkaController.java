package com.jef.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jef.config.KafkaConfig;
import com.jef.entity.User;
import com.jef.service.impl.KafkaUserProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Jef
 * @date 2023/10/30
 */
@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private static final Logger logger = LogManager.getLogger(KafkaController.class);

    @Value("${kafka.topic.my-topic}")
    String myTopic;
    @Value("${kafka.topic.my-topic2}")
    String myTopic2;
    private final KafkaUserProducerService producer;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private KafkaConfig kafkaConfig;

    private AtomicBoolean isRunning = new AtomicBoolean(true);

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

    @GetMapping(value = "/sendMessageToKafkaTopic")
    public String sendMessageToKafkaTopic(String topic, @RequestParam("name") String name) {
        // 1个topic一个分区，顺序发送和消费
        for (int i = 0; i < 10; i++) {
            this.producer.sendMessage(topic, JSONObject.toJSONString(new User(i, name)));
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

    @GetMapping("/pollMessage")
    public String pollMessage(@RequestParam String topic, @RequestParam String groupId) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(kafkaConfig.consumerConfigs(groupId));
        kafkaConsumer.subscribe(Lists.newArrayList(topic));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(5000);
        if (records.isEmpty()) {
            return "noMessage";
        }
        // 处理消息
        processRecords(kafkaConsumer, records);
        // 手动提交
        kafkaConsumer.commitSync();
        return "success";
    }

    @PreDestroy
    void close() {
        logger.info("释放资源");
        isRunning.set(false);
    }

    private void processRecords(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                logger.info("获取消息成功：topic==>{}, partition==>{}, offset==>{}, message==>{}",
                        record.topic(), record.partition(), record.offset(), record.value());
                // doSomething with record
            } catch (Exception e) {
            }
        }
    }
}