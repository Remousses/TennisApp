package com.remousses.app.tennis.model;

public interface ITennisGameInfoEventListener {
    void onData(String event);
    void processComplete();
}
