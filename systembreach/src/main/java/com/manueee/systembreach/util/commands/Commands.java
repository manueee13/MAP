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

    public static String catCommand(FileSystem fs, String path) {
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
