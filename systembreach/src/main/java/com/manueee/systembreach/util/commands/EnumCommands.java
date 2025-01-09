package com.manueee.systembreach.util.commands;

import java.util.stream.Stream;

/**
 * <h2>EnumCommands</h2>
 * Definizione dei comandi del gioco.
 */
public enum EnumCommands {
    INVALID(
        "",
        "Comando non valido"
    ),

    LS(
        "ls",
        "Stampa il contenuto della directory corrente"
    ),

    CD(
        "cd",
        "Cambia la directory corrente"
    ),


    CAT(
        "cat",
        "Stampa il contenuto di un file"
    ),

    CLEAR(
        "clear",
        "Pulisce il terminale"
    ),

    LIST(
        "list",
        "Mostra l'elenco dei comandi disponibili"
    ),

    MANUAL(
        "man",
        "Guida all'uso di un comando"
    ),

    VPN(
        "wp-quick",
        "Connettiti alla VPN"
    ),

    // CHALLENGE
    // SQL INJECTION
    SQLINJECTION(
        "sqlmap",
        ""
    ),

    // REV/PWN
    PWN(
        "gdb",
        ""
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
        return new StringBuilder(command).append(" - ").append(description).append("\n").toString();
    }

    public static EnumCommands fromString(final String command) {
        return Stream.of(EnumCommands.values())
            .filter(c -> c.getCommand().equals(command))
            .findFirst()
            .orElse(EnumCommands.INVALID);
    }
 }
