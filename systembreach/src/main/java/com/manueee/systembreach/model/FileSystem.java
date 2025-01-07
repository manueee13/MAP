package com.manueee.systembreach.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;;

public class FileSystem {
    private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
    }

    /**
     * Carica la struttura del file system da un file JSON.
     * @param jsonFS il JSON che rappresenta il file system
     */
    public void loadFromJson(JsonObject jsonFS) {
        if (jsonFS == null || !jsonFS.has("root")) {
            System.err.println("Errore: JSON non valido. File System non caricato.");
            return;
        }
        JsonObject rootNode = jsonFS.getAsJsonObject("root");
        root = parseDirectory(rootNode, null);
        currentDirectory = root;
    }

    /**
     * Mostra il contenuto della directory corrente o di una directory specificata quando viene passato il comando ls.
     * @param path il percorso della directory
     * @return il contenuto della directory
     */
    public String ls(String path) {
        Directory dir = path == null || path.trim().isEmpty() 
        ? currentDirectory 
        : findDirectory(path);
        if (dir == null) {
            return "ls: cannot access '" + path + "': No such file or directory";
        }
        return String.join(" ", dir.children.keySet());
    }

    /**
     * Crea un file in una directory specifica
     * @param path il percorso del file
     * @param content il contenuto del file
     */
    public void createFile(String path, String content) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        String parentPath = path.substring(0, path.lastIndexOf("/"));

        Directory parentDir = findDirectory(parentPath);
        if (parentDir != null) {
            parentDir.children.put(fileName, new File(fileName, parentDir, content));
        } else {
            throw new IllegalArgumentException("createFile: cannot create file '" + path + "': No such file or directory");
        }
    }

    private Directory findDirectory(String path) {
        FSNode node = findNode(path);
        return node instanceof Directory ? (Directory) node : null;
    }

    private FSNode findNode(String path) {
        FSNode current = path.startsWith("/") ? root : currentDirectory;
        String[] parts = path.split("/");
        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) continue;
            if (part.equals("..")) {
                current = current instanceof Directory && ((Directory) current).parent != null 
                ? ((Directory) current).parent 
                : current;
            } else if (current instanceof Directory) {
                current = ((Directory) current).children.get(part);
            }
            if (current == null) return null;
        }
        return current;
    }

    /**
     * Analizza il JSON e crea una directory.
     *
     * @param dirJson il JSON della directory.
     * @param parent  la directory genitore.
     * @return la directory creata.
     */
    private Directory parseDirectory(JsonObject dirJson, Directory parent) {
        if (dirJson == null || !dirJson.has("name")) {
            return null;
        }

        String name = dirJson.get("name").getAsString();
        Directory dir = new Directory(name, parent);

        if (dirJson.has("children")) {
            JsonObject children = dirJson.getAsJsonObject("children");
            for (String childName : children.keySet()) {
                JsonObject childJson = children.getAsJsonObject(childName);
                if ("file".equals(childJson.get("type").getAsString())) {
                    String content = childJson.has("content") ? childJson.get("content").getAsString() : "";
                    dir.children.put(childName, new File(childName, dir, content));
                } else if ("directory".equals(childJson.get("type").getAsString())) {
                    dir.children.put(childName, parseDirectory(childJson, dir));
                }
            }
        }

        return dir;
    }

    // ========CLASSI NIDIFICATE========

    private abstract class FSNode {
        protected String name;
        protected Directory parent;

        public FSNode(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
        }
    }

    private class File extends FSNode {
        private String content;

        public File(String name, Directory parent, String content) {
            super(name, parent);
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    private class Directory extends FSNode {
        private Map<String, FSNode> children;

        public Directory(String name, Directory parent) {
            super(name, parent);
            this.children = new HashMap<>();
        }
    }
}
