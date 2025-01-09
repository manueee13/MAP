package com.manueee.systembreach.util.sessions;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.manueee.systembreach.model.FileSystem;

/**
 * <h2>SessionUtils</h2>
 * Funzioni per la gestione delle sessioni del gioco.
 */
public class SessionUtils {
    
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

    public static void loadSession(File file) {
        // TODO
    }

    public static void saveSession(File file) {
        // TODO
    }
}
