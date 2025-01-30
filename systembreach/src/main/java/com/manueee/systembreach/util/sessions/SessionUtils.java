package com.manueee.systembreach.util.sessions;

import com.manueee.systembreach.controller.GameController;
import com.manueee.systembreach.model.FileSystem;
import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.util.commands.EnumCommands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Utility class per la gestione delle sessioni di gioco.
 * Gestisce il salvataggio e il caricamento dello stato di gioco,
 * facendo parte del Controller nel pattern MVC.
 */
public final class SessionUtils {
   
    private static final String FILESYSTEM_PATH = "/sessions/filesystem.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private SessionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Classe interna per la serializzazione dei dati di gioco
     */
    private static class GameData {
        private final JsonObject filesystem;
        private final int currentQuestId;
        private final int remainingTime;
        private final Set<String> availableCommands;

        public GameData(
            JsonObject filesystem,
            String currentPath,
            int currentQuestId,
            int remainingTime,
            Set<String> availableCommands) {
            this.filesystem = filesystem;
            this.currentQuestId = currentQuestId;
            this.remainingTime = remainingTime;
            this.availableCommands = availableCommands;
        }
    }
    
    /**
     * Crea una nuova sessione di gioco caricando il filesystem iniziale
     * @return FileSystem inizializzato o vuoto in caso di errore
     */
    public static FileSystem createNewSession() {
        try (InputStream inputStream = SessionUtils.class.getResourceAsStream(FILESYSTEM_PATH);
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            
            FileSystem fs = new FileSystem();
            fs.loadFromJson(GSON.fromJson(reader, JsonObject.class));
            return fs;
            
        } catch (IOException e) {
            System.err.println("Errore creazione sessione: " + e.getMessage());
            return new FileSystem();
        }
    }

    /**
     * Carica una sessione di gioco salvata
     * @param file File di salvataggio
     * @param gameController Controller di gioco
     * @return GameState caricato o null in caso di errore
     */
    public static GameState loadSession(File file, GameController gameController) {
        try (FileReader reader = new FileReader(file)) {
            GameData loadData = GSON.fromJson(reader, GameData.class);
            return createGameState(loadData, gameController);
        } catch (IOException e) {
            System.err.println("Errore caricamento sessione: " + e.getMessage());
            return null;
        }
    }

    /**
     * Salva lo stato corrente del gioco
     * @param gameState Stato del gioco da salvare
     * @param saveFile File di destinazione
     */
    public static void saveSession(GameState gameState, File saveFile) {
        try (FileWriter writer = new FileWriter(saveFile)) {
            GameData saveData = createGameData(gameState);
            GSON.toJson(saveData, writer);
        } catch (IOException e) {
            System.err.println("Errore salvataggio sessione: " + e.getMessage());
        }
    }

    private static GameState createGameState(GameData data, GameController controller) {
        try {
            FileSystem fs = new FileSystem();
            fs.loadFromJson(data.filesystem);
            
            GameState gameState = new GameState(data.currentQuestId, controller);
            gameState.setFileSystem(fs); // Aggiungi questo metodo a GameState
            
            // Carica i comandi disponibili
            data.availableCommands.forEach(cmd -> {
                EnumCommands command = EnumCommands.fromString(cmd);
                if (command != null) {
                    gameState.unlockCommand(command);
                }
            });
            
            gameState.setRemainingTime(data.remainingTime);
            
            return gameState;
        } catch (Exception e) {
            System.err.println("Errore durante la creazione del GameState: " + e.getMessage());
            return null;
        }
    }

    private static GameData createGameData(GameState gameState) {
        try {
            FileSystem fs = gameState.getFileSystem();
            Set<String> availableCommands = gameState.getAvailableCommands()
                .stream()
                .map(EnumCommands::getCommand)
                .collect(Collectors.toSet());

            return new GameData(
                fs.toJson(),
                fs.getCurrentPath(),
                gameState.getCurrentQuestId(),
                gameState.getRemainingTime(),
                availableCommands
            );
        } catch (Exception e) {
            System.err.println("Errore durante la creazione del GameData: " + e.getMessage());
            return null;
        }
    }
}
