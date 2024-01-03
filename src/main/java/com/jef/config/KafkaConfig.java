package com.jef.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.Properties;

/**
 * Kafka配置
 * @author Jef
 * @date 2023/10/30
 */
@Configuration
public class KafkaConfig {
    @Value("${kafka.topic.my-topic}")
    String myTopic;
    @Value("${kafka.topic.my-topic2}")
    String myTopic2;
    @Value("${kafka.topic.my-topic3}")
    String myTopic3;

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;
    @Value("${spring.kafka.consumer.session-timeout}")
    private String sessionTimeout;
    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    /**
     * JSON消息转换器
     */
    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    /**
     * 通过注入一个 NewTopic 类型的 Bean 来创建 topic，如果 topic 已存在，则会忽略。
     */
    @Bean
    public NewTopic myTopic() {
        return new NewTopic(myTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic myTopic2() {
        return new NewTopic(myTopic2, 1, (short) 1);
    }

    @Bean
    public NewTopic myTopic3() {
        return new NewTopic(myTopic3, 1, (short) 1);
    }

    public Properties consumerConfigs() {
        Properties properties = new Properties();
        // 服务器
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        // 是否启动自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        // 自动提交间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        // 会话超时，超过此时间没有发送心跳,则认为消费者死亡,剔除组,并触发rebalance
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 抵消重置
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        // 最大等待时间
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);
        // 请求超时时间
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 21000);
        // 单次拉取消息数
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        // 拉取消息时间间隔
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60000);
        // 心跳时间，一般不做配置
//        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 100);
        return properties;
    }

    public Properties consumerConfigs(String groupId) {
        Properties properties = consumerConfigs();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return properties;
    }
}
