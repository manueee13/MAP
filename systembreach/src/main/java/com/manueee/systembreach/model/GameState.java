package com.manueee.systembreach.model;

import com.manueee.systembreach.model.Timer;

public class GameState {
    private int timeRemaining;
    private boolean isGameOver;

    public GameState(int initialTime) {
        this.timeRemaining = initialTime;
        this.isGameOver = false;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
