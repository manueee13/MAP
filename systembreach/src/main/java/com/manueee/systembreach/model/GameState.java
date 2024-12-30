package com.manueee.systembreach.model;

import java.util.List;

public class GameState {
    private boolean isGameOver;
    private List<String> terminalHistory;
    private List<String> infoHistory;

    public GameState() {
        this.isGameOver = false;
    }

    private List<GameStateObserver> observers;

    public interface GameStateObserver {
        void onGameStateChanged();
    }

    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    /*
    private void notifyObservers() {
        observers.forEach(GameStateObserver::onGameStateChanged);
    }
    */
    
    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
