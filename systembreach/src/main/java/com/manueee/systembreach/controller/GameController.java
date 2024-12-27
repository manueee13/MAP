package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.view.GameView;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
public class GameController {
    private GameState gameState;
    private GameView gameView;
    private Timer timer;
    
    public GameController(GameState gameState, GameView gameView) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.timer = new Timer();
    }
}
