package com.manueee.systembreach.util.commands;

import java.util.stream.Stream;

/**
 * <h2>EnumCommands</h2>
 * Definizione dei comandi del gioco.
 */
public enum EnumCommands {
    INVALID(
        "",
        ""
    ),

    LS(
        "ls",
        "ls <path> : Stampa il contenuto della directory corrente"
    ),

    CD(
        "cd",
        "cd <path> : Cambia la directory corrente"
    ),


    CAT(
        "cat",
        "cat <file> : Stampa il contenuto di un file"
    ),

    CLEAR(
        "clear",
        "clear : Pulisce il terminale"
    ),

    LIST(
        "list",
        "list : Mostra l'elenco dei comandi disponibili"
    ),

    MANUAL(
        "man",
        "man <command> : Guida all'uso di un comando"
    ),

    VPN(
        "wp-quick",
        "Connettiti alla VPN"
    ),

    DECRYPT(
        "fcrackzip",
        "frackzip version 1.0, a fast/free zip password cracker.\n\nUSAGE: fcrackzip\n\t[-b] - use brute-force algorithm\n\t[-D] <path> - use a dictionary\n\n"
    );

    private final String command;
    private final String description;

    EnumCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getCommandInfo() {
        return new StringBuilder(description).append("\n").toString();
    }

    public static EnumCommands fromString(final String command) {
        return Stream.of(EnumCommands.values())
            .filter(c -> c.getCommand().equals(command))
            .findFirst()
            .orElse(EnumCommands.INVALID);
    }
 }
