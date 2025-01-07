package com.manueee.systembreach.util.sessions;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.manueee.systembreach.model.FileSystem;

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
    
    private static FileSystem parseJsonToFileSystem(JsonObject json) {
        FileSystem fs = new FileSystem();
        if (json == null || !json.has("root")) {
            return fs;
        }
    
        JsonObject rootNode = json.getAsJsonObject("root");
        if (rootNode != null && rootNode.has("children")) {
            parseDirectory(rootNode, fs);
        }
        return fs;
    }
    
    private static void parseDirectory(JsonObject dirJson, FileSystem fs) {
        if (dirJson == null || !dirJson.has("type") || !dirJson.has("name")) {
            return;
        }
    
        String type = dirJson.get("type").getAsString();
        String name = dirJson.get("name").getAsString();
        
        if ("directory".equals(type) && dirJson.has("children")) {
            JsonObject children = dirJson.getAsJsonObject("children");
            for (String childName : children.keySet()) {
                JsonObject child = children.getAsJsonObject(childName);
                if (child.has("type")) {
                    if ("file".equals(child.get("type").getAsString())) {
                        String content = child.has("content") ? 
                            child.get("content").getAsString() : "";
                        fs.createFile(name + "/" + childName, content);
                    } else {
                        parseDirectory(child, fs);
                    }
                }
            }
        }
    }

    public static void loadSession(File file) {
        // TODO
    }

    public static void saveSession(File file) {
        // TODO
    }
}
