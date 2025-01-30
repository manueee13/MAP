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
 * Classe model principale che gestisce lo stato del gioco.
 * Mantiene il file system virtuale, i comandi disponibili e lo stato della missione corrente.
 */
public class GameState {
    private FileSystem fileSystem;
    private final Set<EnumCommands> availableCommands;
    private final List<GameStateObserver> observers;
    private boolean isGameOver;
    private int currentQuestId;
    private final GameController gameController;

    /**
     * Costruisce un nuovo stato di gioco
     * @param currentQuestId ID della missione iniziale
     * @param gameController Controller di gioco associato
     * @throws IllegalArgumentException se gameController è null
     */
    public GameState(int currentQuestId, GameController gameController) {
        if (gameController == null) {
            throw new IllegalArgumentException("GameController cannot be null");
        }
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

    public void setFileSystem(FileSystem fs) {
        this.fileSystem = fs;
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

    public HashSet<EnumCommands> getAvailableCommands() {
        return new HashSet<>(availableCommands);
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

    public int getRemainingTime() {
        return gameController.getTime();
    }

    public void setRemainingTime(int time) {
        gameController.setTime(time);
    }
}
