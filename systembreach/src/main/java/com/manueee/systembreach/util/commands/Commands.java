package com.manueee.systembreach.util.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.manueee.systembreach.model.FileSystem;
import com.manueee.systembreach.model.Terminal;

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
        return "Non esiste nessun comando con questo nome, digita 'help' per l'elenco dei comandi disponibili";
    }

    /**
     * Stampa la lista comandi disponibili.
     * @return la lista dei comandi disponibili
     */
    public static String listCommand() {
        //TODO: Fare in modo che visualizzi i comandi disponibili solo nella sessione corrente e non tutti i comandi.
        return Stream.of(EnumCommands.values())
                .map(EnumCommands::getCommand)
                .collect(Collectors.joining(" "));
    }

    public static String lsCommand(FileSystem fs, String path) {
        return fs.ls(path);
    }

    public static String cdCommand(FileSystem fs, String path) {
        if (path == null || path.trim().isEmpty()) {
            return "cd: missing operand";
        }
        if (!fs.cd(path)) {
            return "cd: " + path + ": No such file or directory";
        }
        return "";
    }

    public static String catCommand() {
        return "cat";
    }

    public static void clearCommand(Terminal terminal) {
        terminal.clearOutputBuffer();
    }

    public static String manualCommand() {
        return "man";
    }

    public static String vpnCommand() {
        // TODO
        return "wp-quick";
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
