package com.manueee.systembreach.model;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>FileSystem</h1>
 * Classe <b>model</b> per la gestione del file system di gioco.
 */
public class FileSystem {
    private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
    }

    /**
     * Mostra il contenuto della directory corrente o di una directory specificata quando viene passato il comando ls.
     * @param path il percorso della directory
     * @return il contenuto della directory
     */
    public String ls(String path) {
        if (path == null || path.trim().isEmpty()) {
        }

        FSNode node = findNode(path);
        if (node == null) {
            return "ls: cannot access '" + path + "': No such file or directory";
        }

        if (node instanceof Directory) {
            Directory dir = (Directory) node;
            return String.join(" ", dir.children.keySet());
        } else {
            return node.getName();
        }
    }

    /**
     * Cambia percorso della directory corrente.
     * @param path
     * @return <b>true</b> se la directory esiste, <b>false</b> altrimenti
     */
    public String cd(String path) {
        FSNode node = findNode(path);
        if (node == null) {
            return "NOEXIST";
        }
        if (node instanceof File) {
            return "NOTDIR";
        }
        currentDirectory = (Directory) node;
        return "OK";
    }

    public String cat(String path) {
        FSNode node = findNode(path);
        if (node == null) {
            return "NOEXIST";
        }
        if (node instanceof Directory) {
            return "NOTFILE";
        }
        return ((File) node).getContent();
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
     * <code>createFile</code>
     * Crea un file in una directory specifica.
     * @param path Assegna il percorso del file
     * @param content Restituisce il contenuto del file
     */
    public void createFile(String path, String content, String type) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        String parentPath = path.substring(0, path.lastIndexOf("/"));

        Directory parentDir = findDirectory(parentPath);
        if (parentDir != null) {
            parentDir.children.put(fileName, new File(fileName, parentDir, content, type));
        } else {
            throw new IllegalArgumentException("createFile: cannot create file '"
                    + path
                    + "': No such file or directory");
        }
    }

    private Directory findDirectory(String path) {
        FSNode node = findNode(path);
        return node instanceof Directory ? (Directory) node : null;
    }

    private FSNode findNode(String path) {
        // Se path è vuoto o "." ritorna la directory corrente
        if (path == null || path.isEmpty() || ".".equals(path)) {
        }

        // Se path è ".." vai al parent se esiste
        if ("..".equals(path)) {
            return currentDirectory.parent != null ? currentDirectory.parent : currentDirectory;
        }
    
        // Determina il punto di partenza
        FSNode current = path.startsWith("/") ? root : currentDirectory;
        path = path.startsWith("/") ? path.substring(1) : path;
    
        // Dividi il path e naviga
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                if (current.parent != null) {
                    current = current.parent;
                }
                continue;
            }
            if (!(current instanceof Directory)) {
                return null;
            }
            Directory dir = (Directory) current;
            FSNode next = dir.children.get(part);
            if (next == null) {
                return null;
            }
            current = next;
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
        if (dirJson == null || !dirJson.has("name") || !dirJson.has("type")|| 
            !"directory".equals(dirJson.get("type").getAsString())) {
            return null;
        }

        String name = dirJson.get("name").getAsString();
        Directory dir = new Directory(name, parent);

        if (dirJson.has("children")) {
            JsonObject children = dirJson.getAsJsonObject("children");
            for (String childName : children.keySet()) {
                JsonObject childJson = children.getAsJsonObject(childName);
                childJson.addProperty("name", childName);
                String type = childJson.get("type").getAsString();
                
                switch(type) {
                    case "directory":
                        Directory childDir = parseDirectory(childJson, dir);
                        dir.children.put(childName, childDir);
                        break;
                    default:
                        String content = childJson.has("content") ? childJson.get("content").getAsString() : "";
                        dir.children.put(childName, new File(childName, dir, content, type));
                        break;
                }
            }
        }
        return dir;
    }

    // ========CLASSI NIDIFICATE========

    private abstract class FSNode {
        protected String name;
        protected Directory parent;
        protected String type;

        public FSNode(String name, Directory parent, String type) {
            this.name = name;
            this.parent = parent;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    private class File extends FSNode {
        private String content;

        public File(String name, Directory parent, String content, String type) {
            super(name, parent, type);
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    private class Directory extends FSNode {
        private Map<String, FSNode> children = new HashMap<>();

        public Directory(String name, Directory parent) {
            super(name, parent, "directory");
        }

        public Map<String, FSNode> getChildren() {
            return children;
        }
    }
}
