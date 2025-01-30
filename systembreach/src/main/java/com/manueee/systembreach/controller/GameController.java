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
 * Controller principale del gioco che gestisce l'interazione tra view e model.
 * Gestisce il timer di gioco, le mail e il salvataggio della sessione.
 * Implementa TimerListener per gestire gli eventi del timer.
 */
public class GameController implements Timer.TimerListener {
    private static final int TIMER_DURATION = 1800;  // 30 minuti
    private static final int SECONDS_IN_A_MINUTE = 60;

    private final GameState gameState;
    private final GameView gameView;
    private Timer gameTimer;
    private final CommandController commandController;
    
    /**
     * Inizializza un nuovo controller di gioco
     * @param isNewGame true per una nuova partita, false per caricare una sessione salvata
     * @param loadFile file di salvataggio da caricare (può essere null se isNewGame è true)
     */
    public GameController(boolean isNewGame, File loadFile) {
        if (isNewGame) {
            this.gameState = new GameState(1, this);
            this.gameTimer = new Timer(TIMER_DURATION, this);
        } else {
            this.gameState = SessionUtils.loadSession(loadFile, this);
            this.gameTimer = new Timer(gameState.getRemainingTime(), this); // Inizializza con il tempo salvato
        }
        
        this.commandController = new CommandController(new Terminal(), gameState);
        this.gameView = new GameView(commandController, this);
        
        initializeGame();
        
        if (isNewGame) {
            notifyNewMail(gameState.getMail(1), 1);
        } else {
            // Ricrea la lista delle mail precedenti
            for (int i = 1; i <= gameState.getCurrentQuestId(); i++) {
                Mail mail = gameState.getMail(i);
                notifyNewMail(mail, i);
            }
        }
        
        gameView.setVisible(true);
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
        if (gameTimer != null) {
            gameTimer.stopTimer();
        }
        gameTimer = new Timer(time, this);
        gameTimer.start();
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
