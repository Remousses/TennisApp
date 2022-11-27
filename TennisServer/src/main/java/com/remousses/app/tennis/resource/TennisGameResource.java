package com.remousses.app.tennis.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remousses.app.tennis.TennisScheduler;
import com.remousses.app.tennis.service.TennisGameService;


@RestController
@RequestMapping("/game/")
public class TennisGameResource {
	
	@Autowired
	private TennisScheduler tennisScheduler;
	
	@Autowired
	private TennisGameService tennisGameService;
	
	@GetMapping(value = "ping")
	public String ping() {
		return "pong";
	}
	
	@GetMapping(value = "new-game", produces = MediaType.ALL_VALUE)
	public String newGame() {
		tennisGameService.initializeGame();
		return tennisScheduler.startSchedule();
	}
	
	@GetMapping(value = "stop-game")
	public String stopGame() {
		return tennisScheduler.stopSchedule();
	}
}
