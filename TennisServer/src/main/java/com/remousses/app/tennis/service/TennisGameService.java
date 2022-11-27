package com.remousses.app.tennis.service;

import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remousses.app.tennis.TennisScheduler;
import com.remousses.app.tennis.kafka.TennisGameProducer;
import com.remousses.app.tennis.model.TennisGameInfo;
import com.remousses.app.tennis.util.Constant;
import com.remousses.app.tennis.util.Score;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TennisGameService {
	private TennisGameInfo tennisGameInfo;
	
	@Autowired
	private TennisScheduler tennisScheduler;
	
	@Autowired
	private TennisGameProducer tennisGameProducer;
	
	public void initializeGame() {
    	this.tennisGameInfo = new TennisGameInfo(Constant.WINS[0], Constant.WINS[1]);
	}
	
	public void startNewGame() {
		final String winPoint = Constant.WINS[new Random().nextInt(Constant.WINS.length)];
        
		this.computeGame(winPoint);

        if (this.hasWinner()) {
        	tennisScheduler.stopSchedule();
        }
        
       log.info(this.getScore());
       tennisGameProducer.sendMessage(this.getScore());
	}
    
    public String getScore() {
        if (hasWinner()) {
            return playerWithHighestScore() + " wins";
        }

        if (isDeuce()) {
            return "DEUCE";
        }

        if (hasAdvantage()) {
            return "ADVANTAGE " + playerWithHighestScore();
        }

        if(tennisGameInfo.getScorePlayer1() == tennisGameInfo.getScorePlayer2()) {
            return translateScore(tennisGameInfo.getScorePlayer1()) + " all";
        }

        return tennisGameInfo.getPlayer1() + " " + translateScore(tennisGameInfo.getScorePlayer1()) + " - " + tennisGameInfo.getPlayer2() + " " + translateScore(tennisGameInfo.getScorePlayer2());
    }

    private boolean isDeuce() {
        return tennisGameInfo.getScorePlayer1() >= Score.FORTY.getPos() && tennisGameInfo.getScorePlayer2() == tennisGameInfo.getScorePlayer1();
    }

    private String playerWithHighestScore() {
        return tennisGameInfo.getScorePlayer1() > tennisGameInfo.getScorePlayer2() ? tennisGameInfo.getPlayer1() : tennisGameInfo.getPlayer2();
    }

    public boolean hasWinner() {
        if(tennisGameInfo.getScorePlayer2() >= Score.OVER.getPos() && tennisGameInfo.getScorePlayer2() >= tennisGameInfo.getScorePlayer1() + 2) {
            return true;
        }
        return tennisGameInfo.getScorePlayer1() >= Score.OVER.getPos() && tennisGameInfo.getScorePlayer1() >= tennisGameInfo.getScorePlayer2() + 2;
    }

    private boolean hasAdvantage() {
        if (tennisGameInfo.getScorePlayer2() >= Score.OVER.getPos() && tennisGameInfo.getScorePlayer2() == tennisGameInfo.getScorePlayer1() + 1) {
            return true;
        }
        return tennisGameInfo.getScorePlayer1() >= Score.OVER.getPos() && tennisGameInfo.getScorePlayer1() == tennisGameInfo.getScorePlayer2() + 1;
    }

    public void computeGame(final String win) {
        if (win.equals(tennisGameInfo.getPlayer1())) {
        	tennisGameInfo.setScorePlayer1(tennisGameInfo.getScorePlayer1() + 1);
        } else {
        	tennisGameInfo.setScorePlayer2(tennisGameInfo.getScorePlayer2() + 1);
        }
    }

    private String translateScore(final int score) {
        switch (getScoreEnum(score)) {
            case FORTY:
                return Score.FORTY.getScore();
            case THIRTY:
                return Score.THIRTY.getScore();
            case FIFTEEN:
                return Score.FIFTEEN.getScore();
            case DEFAULT:
                return Score.DEFAULT.getScore();
            default:
                throw new IllegalArgumentException("Illegal score: " + score);
        }
    }

    private Score getScoreEnum(final Integer pos) {
        return Arrays.stream(Score.values())
                .filter(p -> p.getPos() == pos)
                .findFirst().orElse(Score.DEFAULT);
    }
}