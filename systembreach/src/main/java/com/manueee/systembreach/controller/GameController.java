package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.model.GameTimer;
import com.manueee.systembreach.view.GameView;

public class GameController implements GameTimer.TimerListener {
    private final GameState gameState;
    private final GameView gameView;
    private final GameTimer gameTimer;
    
    public GameController(GameState gameState, GameView gameView) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.gameTimer = new GameTimer(1800, this); // 1800 = 30 minuti

        initializeGame();
    }

    private void initializeGame() {
        gameTimer.start();
    }

    @Override
    public void onTimeUpdate(int secondsLeft) {
        String formattedTime = String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60);
        gameView.updateTimer(formattedTime);
    }

    @Override
    public void onTimeOut() {
        gameState.setGameOver(true);
        //gameView.showGameOverDialog();
    }
}
