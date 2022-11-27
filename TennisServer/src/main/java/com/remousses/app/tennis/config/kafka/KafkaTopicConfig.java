package com.remousses.app.tennis.config.kafka;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

public class KafkaTopicConfig extends KafkaConfig {
	
    @Value(value = "${spring.kafka.topic.name}")
    private String topicName;
    
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = sharedConfig();
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic topic1() {
         return new NewTopic(topicName, 1, (short) 1);
    }
}