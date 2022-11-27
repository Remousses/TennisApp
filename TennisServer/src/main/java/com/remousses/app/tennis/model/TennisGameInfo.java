package com.remousses.app.tennis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TennisGameInfo {
	private String player1;
	private String player2;
	private int scorePlayer1;
	private int scorePlayer2;
	
	public TennisGameInfo(String player1, String player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
}
