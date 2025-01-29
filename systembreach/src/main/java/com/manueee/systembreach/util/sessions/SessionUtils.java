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
import com.google.gson.JsonSyntaxException;

/**
 * <h2>SessionUtils</h2>
 * Funzioni per la gestione delle sessioni del gioco.
 */
public class SessionUtils {
   
    private static class GameData {
        private JsonObject filesystem;
        private int currentQuestId;
        private int remainingTime;
        private Set<String> availableCommands;

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
    
    public static FileSystem createNewSession() {
        try (InputStream inputStream = SessionUtils.class.getResourceAsStream("/sessions/filesystem.json");
        InputStreamReader reader = new InputStreamReader(inputStream)) {
            Gson gson = new Gson();
            JsonObject jsonFS = gson.fromJson(reader, JsonObject.class);
            FileSystem fs = new FileSystem();
            fs.loadFromJson(jsonFS);
            return fs;
        } catch (Exception e) {
            System.err.println("Errore parsing JSON: " + e.getMessage());
            return new FileSystem();
        }
    }

    public static GameState loadSession(File file, GameController gameController) {
        try {
            Gson gson = new Gson();
            GameData loadData = gson.fromJson(new FileReader(file), GameData.class);

            FileSystem fs = new FileSystem();
            fs.loadFromJson(loadData.filesystem);

            GameState gameState = new GameState(loadData.currentQuestId, gameController);

            loadData.availableCommands.forEach(cmd -> gameState.unlockCommand(EnumCommands.fromString(cmd)));

            gameState.setRemainingTime(loadData.remainingTime);

            return gameState;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Errore caricamento sessione: " + e.getMessage());
            return null;
        }
    }

    public static void saveSession(GameState gameState, File saveFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileSystem fs = gameState.getFileSystem();
        Set<String> availableCommands = gameState.getAvailableCommands()
            .stream()
            .map(EnumCommands::getCommand)
            .collect(Collectors.toSet());

        GameData saveData = new GameData(
            fs.toJson(),
            fs.getCurrentPath(),
            gameState.getCurrentQuestId(),
            gameState.getRemainingTime(),
            availableCommands
        );

        try (FileWriter writer = new FileWriter(saveFile)) {
            gson.toJson(saveData, writer);
        } catch (IOException e) {
            System.err.println("Errore salvataggio sessione: " + e.getMessage());
        }
    }
}
