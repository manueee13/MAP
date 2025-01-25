package com.manueee.systembreach.model;

import com.manueee.systembreach.util.sessions.QuestUtils;
import com.manueee.systembreach.util.sessions.SessionUtils;
import com.manueee.systembreach.util.commands.EnumCommands;
import com.manueee.systembreach.controller.GameController;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * <h2>GameState</h2>
 * Classe <b>model</b> per la gestione dello stato del gioco.
 */
public class GameState {
    private final FileSystem fileSystem;
    private boolean isGameOver;
    private List<GameStateObserver> observers;
    private final Set<EnumCommands> availableCommands;
    private int currentQuestId;
    private final GameController gameController;

    public GameState(int currentQuestId, GameController gameController) {
        this.isGameOver = false;
        this.observers = new ArrayList<>();
        this.fileSystem = SessionUtils.createNewSession();
        this.availableCommands = new HashSet<>();
        this.currentQuestId = currentQuestId;
        this.gameController = gameController;
        initializeCommands();
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    private void initializeCommands() {
        availableCommands.add(EnumCommands.LIST);
        availableCommands.add(EnumCommands.LS);
        availableCommands.add(EnumCommands.CD);
        availableCommands.add(EnumCommands.CAT);
        availableCommands.add(EnumCommands.CLEAR);
        availableCommands.add(EnumCommands.MANUAL);
        availableCommands.add(EnumCommands.DECRYPT);
    }

    public boolean isCommandAvailable(EnumCommands command) {
        return availableCommands.contains(command);
    }

    public void unlockCommand(EnumCommands command) {
        availableCommands.add(command);
        notifyObservers();
    }

    public Mail getMail(int questId) {
        return QuestUtils.getMail(questId);
    }

    public interface GameStateObserver {
        void onGameStateChanged();
    }

    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        observers.forEach(GameStateObserver::onGameStateChanged);
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        if (this.isGameOver != gameOver) {
            this.isGameOver = gameOver;
            notifyObservers();
        }
    }

    public int getCurrentQuestId() {
        return currentQuestId;
    }

    public void advanceQuest() {
        currentQuestId++;
        gameController.notifyNewMail(getMail(currentQuestId), currentQuestId);
        notifyObservers();
    }
}
