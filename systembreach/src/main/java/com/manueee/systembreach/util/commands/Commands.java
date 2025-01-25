package com.manueee.systembreach.util.commands;

import com.manueee.systembreach.model.FileSystem;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.model.GameState;

import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * <h2>Commands</h2>
 * Funzioni dei comandi del gioco.
 */
public final class Commands {

    private Commands() {

    }

    /**
     * Stamnpa l'errore di comando non valido.
     * @return l'errore di comando non valido
     */
    public static String invalidCommand() {
        return "Non esiste nessun comando con questo nome, digita 'list' per l'elenco dei comandi disponibili";
    }

    /**
     * Stampa la lista comandi disponibili.
     * @return la lista dei comandi disponibili
     */
    public static String listCommand(GameState gameState) {
        return Stream.of(EnumCommands.values())
                .filter(cmd -> gameState.isCommandAvailable(cmd))
                .map(EnumCommands::getCommand)
                .filter(cmd -> !cmd.isEmpty()) // Filtra INVALID
                .collect(Collectors.joining(" "));
    }

    public static String lsCommand(FileSystem fs, String path) {
        return fs.ls(path);
    }

    public static String cdCommand(FileSystem fs, String path) {
        if (path == null || path.trim().isEmpty()) {
            return "cd: missing operand";
        }
        String result = fs.cd(path);
        switch (result) {
            case "NOEXIST":
                return "cd: " + path + ": No such file or directory";
            case "NOTDIR":
                return "cd: " + path + ": Not a directory";
            case "OK":
                return "";
            default:
                return "cd: unknown error";
        }
    }

    public static String catCommand(GameState gameState, String path) {
        FileSystem fs = gameState.getFileSystem();

        if (path == null || path.trim().isEmpty()) {
            return "cat: missing operand";
        }

        String result = fs.cat(path);
        switch (result) {
            case "NOEXIST":
                return "cat: " + path + ": No such file or directory";
            case "NOTFILE":
                return "cat: " + path + ": Is a directory";
            default:
                return result;
        }

    }

    public static void clearCommand(Terminal terminal) {
        terminal.clearOutputBuffer();
    }

    public static String manualCommand(String cmd) {
        if (cmd == null || cmd.trim().isEmpty()) {
            StringBuilder result = new StringBuilder();
            Stream.of(EnumCommands.values()).filter(command -> !command.equals(EnumCommands.INVALID)).forEach(command -> result.append(command.getCommandInfo()));
        return result.toString();
        }

        EnumCommands command = EnumCommands.fromString(cmd);
        if (cmd.equals(EnumCommands.INVALID.toString())) {
            return "man: " + cmd + ": No manual entry or invalid command";
        }

        return command.getCommandInfo();
    }

    public static String decryptCommand(GameState gameState, String args) {
        FileSystem fs = gameState.getFileSystem();

        if (args == null || args.trim().isEmpty()) {
            return EnumCommands.DECRYPT.getCommandInfo();
        }
        
        String [] params = args.trim().split("\\s+");
        if (params.length < 1) {
            return "fcrackzip: missing operand";
        }

        boolean isBruteForce = false;
        String dictionaryPath = null;
        String archivePath = null;

        for (int i = 0; i < params.length; i++) {
            switch (params[i]) {
                case "-b":
                    isBruteForce = true;
                    break;
                case "-D":
                    if (i + 1 < params.length) {
                        dictionaryPath = params[++i];
                    } else {
                        return "fcrackzip: missing dictionary path";
                    }
                    break;
                default:
                    if (params[i].endsWith(".zip")) {
                        archivePath = params[i];
                    } else {
                        return "fcrackzip: invalid option or file not recognized";
                    }
            }
        }

        // Check archivio e dizionario esistono
        if (archivePath == null) {
            return "fcrackzip: missing archive path";
        }
        
        String archiveNode = fs.getNode(archivePath);

        if (archiveNode == null && !archivePath.equals("docs.zip")) {
            return "fcrackzip: invalid archive";
        }

        if (isBruteForce) {
            return "> L'attacco bruteforce richiede troppo tempo, devo trovare un'altra alternativa...";
        }

        if (dictionaryPath != null) {
            String dictionaryNode = fs.getNode(dictionaryPath);
            if (dictionaryNode == null && !dictionaryPath.equals("dictionary.txt")) {
                return "fcrackzip: illegal dictionary file";
            }

            int currentQuestId = gameState.getCurrentQuestId();
            if (currentQuestId == 1) {
                gameState.advanceQuest();
                gameState.unlockCommand(EnumCommands.SQLINJECT);
                fs.createFile(
                    "/mnt/sda1/docs/confidentiality.txt",
                    "CONFIDENZIALE\n\nIN CASO DI ESECUZIONE INVOLONTARIA DEL PROGRAMMA COLLEGARSI AL SITO ONION 'http://6h5b7q4op9jf2k1d.onion' E INSERIRE LE CREDENZIALI FORNITE."
                );
                return "fcrackzip: archive successfully decrypted. The content is allocated in the current directory.\n\n> C'è l'ho fatta! Ora devo leggere la documentazione!";
            }
            
        }
        
    return "fcrackzip: unknown error";    
}

    public static String sqlCommand() {
        // TODO
        return "sql";
    }

    public static String pwnCommand() {
        // TODO
        return "pwn";
    }
}
