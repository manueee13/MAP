package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.model.Timer;
import com.manueee.systembreach.model.Mail;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.view.GameView;
import com.manueee.systembreach.view.MailView;
import com.manueee.systembreach.util.sessions.SessionUtils;

import java.io.File;
/**
 * <h1>Classe GameController</h1>
 * Classe <b>controller</b> per l'interazione tra {@link #gameView} e <b>model</b>.
 */
public class GameController implements Timer.TimerListener {
    private static final int TIMER_DURATION = 1800;  // 1800 = 30 minuti
    private static final int SECONDS_IN_A_MINUTE = 60;

    private GameState gameState;
    private GameView gameView;
    private Timer gameTimer;
    private CommandController commandController;
    
    /**
     * <code>GameController</code>
     * Costruttore per la classe GameController.
     * @param isNewGame booleano se carica un nuova sessione o una salvata
     */
    public GameController(boolean isNewGame, File loadFile) {
        if (isNewGame) {
            this.gameState = new GameState(1, this);
            this.commandController = new CommandController(new Terminal(), gameState);
            this.gameView = new GameView(commandController, this);
            this.gameTimer = new Timer(TIMER_DURATION, this);
            initializeGame();
            gameView.setVisible(true);
            notifyNewMail(gameState.getMail(1), 1);
        } else {
            this.gameState = SessionUtils.loadSession(loadFile, this);
            this.commandController = new CommandController(new Terminal(), gameState);
            this.gameView = new GameView(commandController, this);
            initializeGame();
            gameView.setVisible(true);
            viewMail(gameState.getCurrentQuestId());
            
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
        gameView.showGameOverDialog();
    }

    public int getTime() {
        return gameTimer.getCurrentTime();
    }

    public void setTime(int time) {
        this.gameTimer = new Timer(TIMER_DURATION, this);
    }

    public void notifyNewMail(Mail mail, int questId) {
        gameView.addMailEntry(questId, mail.getSender(), mail.getObject());
    }

    public void viewMail(int index) {
        Mail mail = gameState.getMail(index);
        String sender = mail.getSender();
        String object = mail.getObject();
        String content = mail.getContent();
        MailView mailView = new MailView(gameView, sender, object, content);
        mailView.setVisible(true);
    }

    public void saveSession(File file) {
        try {
            SessionUtils.saveSession(gameState, file);
            //gameView.showMessage("Sessione salvata correttamente.");
        } catch (Exception e) {
            System.err.println("Errore salvataggio sessione: " + e.getMessage());
        }
    }
}
