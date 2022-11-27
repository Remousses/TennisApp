package com.remousses.app.tennis.kafka;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.remousses.app.tennis.model.ITennisGameInfoEventListener;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TennisGameConsumer {

	public void listenGroup(String score) {
		System.out.println("Received Message in group : " + score);
	}
	
    private ITennisGameInfoEventListener listener;

    public void register(ITennisGameInfoEventListener listener) {
         this.listener = listener;
    }

    public void onEvent(String event) {
         if (listener != null) {
              listener.onData(event);
         }
     }

    public void onComplete() {
        if (listener != null) {
             listener.processComplete();
        }
    }

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.group.id}")
    public void consume(String score) throws IOException {
         log.info(String.format("#### -> Consumed message -> %s", score));
         onEvent(score);
    }
}
