package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.model.GameTimer;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.view.GameView;

/**
 * <h1>Classe GameController</h1>
 * Classe <b>controller</b> per l'interazione tra {@link #gameView} e <b>model</b>.
 */
public class GameController implements GameTimer.TimerListener {
    private static final int TIMER_DURATION = 1800;  // 1800 = 30 minuti
    private static final int SECONDS_IN_A_MINUTE = 60;

    private GameState gameState;
    private GameView gameView;
    private GameTimer gameTimer;
    private CommandController commandController;

    /**
     * <code>GameController</code>
     * Costruttore per la classe GameController.
     * @param isNewGame booleano se carica un nuova sessione o una salvata
     */
    public GameController(boolean isNewGame) {
        if (isNewGame) {
            this.gameState = new GameState();
            this.commandController = new CommandController(new Terminal(), gameState);
            this.gameView = new GameView(commandController);
            this.gameTimer = new GameTimer(TIMER_DURATION, this);
            initializeGame();
            gameView.setVisible(true);
        } else {
            // TODO: Carica sessione dal file salvataggio
            /*
            this.gameState = loadGameState();
            this.gameView = new GameView();
            initializeGame();
            */
        }
    }

    private void initializeGame() {
        gameTimer.start();
    }

    @Override
    public void onTimeUpdate(int secondsLeft) {
        String formattedTime = String.format("%02d:%02d",
                secondsLeft / SECONDS_IN_A_MINUTE,
                secondsLeft % SECONDS_IN_A_MINUTE);
        gameView.updateTimer(formattedTime);
    }

    @Override
    public void onTimeOut() {
        gameState.setGameOver(true);
        //gameView.showGameOverDialog();
    }
}
