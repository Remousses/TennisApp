package com.remousses.app.tennis.config.taskscheduler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.remousses.app.tennis.service.TennisGameService;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {
	
	@Autowired
	private TennisGameService tennisGameService;
	
	@PostConstruct
	public void init() {
		tennisGameService.initializeGame();
	}
	
	@Scheduled(fixedRate = 3000)
	public void scheduleByFixedRate() throws Exception {
		tennisGameService.startNewGame();
	}
}