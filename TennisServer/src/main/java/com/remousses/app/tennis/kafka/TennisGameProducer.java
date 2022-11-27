package com.remousses.app.tennis.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TennisGameProducer {

    @Value(value = "${spring.kafka.topic.name}")
    private String topicName;

	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	public TennisGameProducer() {
		
	}

    public ListenableFuture<SendResult<String, String>> sendMessage(String topic, String score) {
         log.info(String.format("#### -> Producing message -> %s", score));
         return this.kafkaTemplate.send(topic, score);
    }

	public void sendMessage(String score) {
		ListenableFuture<SendResult<String, String>> future = 
	      kafkaTemplate.send(topicName, score);
		
	    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	        @Override
	        public void onSuccess(SendResult<String, String> result) {
	            log.info("Sent message=[" + score + 
	  	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            log.info("Unable to send message=[" 
	              + score + "] due to : " + ex.getMessage());
	        }
	    });
	}
}
