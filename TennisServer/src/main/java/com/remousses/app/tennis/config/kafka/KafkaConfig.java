package com.remousses.app.tennis.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    
    protected Map<String, Object> sharedConfig() {
    	Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", bootstrapAddress);
        
        return configs;
    }
}
