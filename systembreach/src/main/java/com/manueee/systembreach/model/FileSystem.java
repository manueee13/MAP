package com.manueee.systembreach.model;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe che implementa un file system virtuale per il gioco.
 * Gestisce la struttura delle directory e dei file, permettendo la navigazione
 * e la manipolazione del contenuto.
 */
public class FileSystem {
    private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        root = new Directory("/", null);
        currentDirectory = root;
    }

    /**
     * Mostra il contenuto di una directory.
     * @param path Il percorso della directory da visualizzare
     * @return Una stringa contenente l'elenco dei file e directory separati da spazi,
     *         o un messaggio di errore se il percorso non esiste
     */
    public String ls(String path) {
        if (path == null || path.trim().isEmpty()) {
            return ls(".");
        }

        FSNode node = findNode(path);
        if (node == null) {
            return "ls: cannot access '" + path + "': No such file or directory";
        }

        if (node instanceof Directory) {
            Directory dir = (Directory) node;
            return String.join(" ", dir.getChildren().keySet());
        } else {
            return node.getName();
        }
    }

    /**
     * Cambia la directory corrente.
     * @param path Il percorso della directory di destinazione
     * @return "OK" se il cambio è avvenuto con successo,
     *         "NOEXIST" se il percorso non esiste,
     *         "NOTDIR" se il percorso non è una directory
     */
    public String cd(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "NOEXIST";
        }

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

    /**
     * Visualizza il contenuto di un file.
     * @param path Il percorso del file da leggere
     * @return Il contenuto del file se esiste,
     *         "NOEXIST" se il file non esiste,
     *         "NOTFILE" se il percorso indica una directory
     */
    public String cat(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "NOEXIST";
        }

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
     * Verifica se un percorso corrisponde a un file valido.
     * @param path Il percorso da verificare
     * @return true se il percorso corrisponde a un file, false altrimenti
     */
    public boolean isValidFile(String path) {
        FSNode node = findNode(path);
        return node instanceof File;
    }

    /**
     * Carica un file system da una rappresentazione JSON.
     * @param jsonFS L'oggetto JSON contenente la struttura del file system
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
     * Converte il file system in una rappresentazione JSON.
     * @return Un oggetto JsonObject che rappresenta il file system
     */
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.add("root", directoryToJson(this.root));
        return root;
    }

    private JsonObject directoryToJson(Directory dir) {
        JsonObject dirJson = new JsonObject();
        dirJson.addProperty("type", "directory");
        dirJson.addProperty("name", dir.getName());

        JsonObject children = new JsonObject();
        for (Map.Entry<String, FSNode> entry : dir.getChildren().entrySet()) {
            FSNode child = entry.getValue();
            if (child instanceof Directory) {
                children.add(entry.getKey(), directoryToJson((Directory) child));
            } else {
                File file = (File) child;
                JsonObject fileJson = new JsonObject();
                fileJson.addProperty("type", "file");
                fileJson.addProperty("name", file.getName());
                fileJson.addProperty("content", file.getContent());
                children.add(entry.getKey(), fileJson);
            }
        }
        dirJson.add("children", children);
        return dirJson;
    }

    /**
     * Crea un nuovo file nel file system.
     * @param path Il percorso completo del file da creare
     * @param content Il contenuto da inserire nel file
     * @throws IllegalArgumentException se la directory padre non esiste
     */
    public void createFile(String path, String content) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        if (content == null) {
            content = "";
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        String parentPath = path.substring(0, path.lastIndexOf("/"));

        Directory parentDir = findDirectory(parentPath);
        if (parentDir != null) {
            parentDir.addChild(fileName, new File(fileName, parentDir, content));
        } else {
            throw new IllegalArgumentException("createFile: cannot create file '"
                    + path
                    + "': No such file or directory");
        }
    }

    /**
     * Crea una nuova directory nel file system.
     * @param path Il percorso completo della directory da creare
     * @throws IllegalArgumentException se la directory padre non esiste
     */
    public void createDirectory(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }

        String dirName = path.substring(path.lastIndexOf("/") + 1);
        String parentPath = path.substring(0, path.lastIndexOf("/"));

        Directory parentDir = findDirectory(parentPath);
        if (parentDir != null) {
            parentDir.addChild(dirName, new Directory(dirName, parentDir));
        } else {
            throw new IllegalArgumentException("createDirectory: cannot create directory '"
                    + path
                    + "': No such file or directory");
        }
    }

    /**
     * Ottiene il percorso della directory corrente.
     * @return Il percorso assoluto della directory corrente
     */
    public String getCurrentPath() {
        StringBuilder path = new StringBuilder();
        Directory current = currentDirectory;

        if (current == root) {
            return "/";
        }

        while (current != root) {
            if (current != root) {
                path.insert(0, current.getName());
                path.insert(0, "/");
            }
            current = current.getParent();
        }

        return path.toString();
    }
    
    /**
     * Ottiene il nome di un nodo del file system.
     * @param path Il percorso del nodo
     * @return Il nome del nodo se esiste, null altrimenti
     */
    public String getNode(String path) {
        return findNode(path).getName();
    }

    private Directory findDirectory(String path) {
        FSNode node = findNode(path);
        return node instanceof Directory ? (Directory) node : null;
    }

    private FSNode findNode(String path) {
        // Se path è vuoto o "." ritorna la directory corrente
        if (path == null || path.isEmpty() || ".".equals(path)) {
            return currentDirectory;
        }

        // Se path è ".." vai al parent se esiste
        if ("..".equals(path)) {
            return currentDirectory.getParent() != null ? currentDirectory.getParent() : currentDirectory;
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
                if (current.getParent() != null) {
                    current = current.getParent();
                }
                continue;
            }
            if (!(current instanceof Directory)) {
                return null;
            }
            Directory dir = (Directory) current;
            FSNode next = dir.getChild(part);
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
                        dir.addChild(childName, childDir);
                        break;
                    default:
                        String content = childJson.has("content") ? childJson.get("content").getAsString() : "";
                        dir.addChild(childName, new File(childName, dir, content));
                        break;
                }
            }
        }
        return dir;
    }

    /**
     * Classe astratta che rappresenta un nodo nel file system.
     * Può essere specializzata in File o Directory.
     */
    private abstract class FSNode {
        private final String name;
        private Directory parent;

        public FSNode(String name, Directory parent) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        protected Directory getParent() {
            return parent;
        }
    }

    /**
     * Classe che rappresenta un file nel file system.
     * Contiene un nome, un riferimento alla directory padre e il contenuto.
     */
    private class File extends FSNode {
        private String content;

        public File(String name, Directory parent, String content) {
            super(name, parent);
            this.content = content != null ? content : "";
        }

        public String getContent() {
            return content;
        }
    }

    /**
     * Classe che rappresenta una directory nel file system.
     * Contiene un nome, un riferimento alla directory padre e una mappa dei figli.
     */
    private class Directory extends FSNode {
        private final Map<String, FSNode> children;

        public Directory(String name, Directory parent) {
            super(name, parent);
            this.children = new HashMap<>();
        }

        public void addChild(String name, FSNode node) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Child name cannot be null or empty");
            }
            if (node == null) {
                throw new IllegalArgumentException("Child node cannot be null");
            }
            children.put(name, node);
        }

        public FSNode getChild(String name) {
            return children.get(name);
        }

        public Map<String, FSNode> getChildren() {
            return new HashMap<>(children);
        }
    }
}
