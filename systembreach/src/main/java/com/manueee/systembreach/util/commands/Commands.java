package com.manueee.systembreach.util.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
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

    public static String lsCommand() {
        return "ls";
    }

    public static String cdCommand() {
        return "cd";
    }

    public static String catCommand() {
        return "cat";
    }

    public static String clearCommand() {
        return "clear";
    }

    public static String manualCommand() {
        return "man";
    }

    public static String vpnCommand() {
        return "wp-quick";
    }

    public static String sqlCommand() {
        return "sql";
    }

    public static String pwnCommand() {
        return "pwn";
    }
}
