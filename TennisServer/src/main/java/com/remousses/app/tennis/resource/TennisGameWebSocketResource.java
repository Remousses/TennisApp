package com.remousses.app.tennis.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remousses.app.tennis.kafka.TennisGameConsumer;
import com.remousses.app.tennis.model.ITennisGameInfoEventListener;

import reactor.core.publisher.Flux;

@RestController("/")
public class TennisGameWebSocketResource {

     @Autowired
     private TennisGameConsumer processor;

     private Flux<String> bridge;

     public TennisGameWebSocketResource() {
          this.bridge = createBridge().publish().autoConnect().cache(10).log();
     }

     @GetMapping(value = "/flux/score", produces = "text/event-stream;charset=UTF-8")
     public Flux<String> getTennisGameScoreInfo() {
          return bridge;
     }

     private Flux<String> createBridge() {
          Flux<String> bridge = Flux.create(sink -> {
               processor.register(new ITennisGameInfoEventListener() {
                   @Override
                   public void processComplete() {
                       sink.complete();
                   }

                   @Override
                   public void onData(String data) {
                       sink.next(data);
                   }
                });
          });
          return bridge;
     }
}