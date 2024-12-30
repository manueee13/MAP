package com.manueee.systembreach.util.commands;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Definizione dei comandi del gioco.
 */
public enum EnumCommands {
    INVALID(
        "",
        "Comando non valido",
        () -> Commands.invalidCommand()
    ),

    LS(
        "ls",
        "Stampa il contenuto della directory corrente",
        () -> Commands.lsCommand()    
    ),

    CD(
        "cd",
        "Cambia la directory corrente",
        () -> Commands.cdCommand()
    ),


    CAT(
        "cat",
        "Stampa il contenuto di un file",
        () -> Commands.catCommand()
    ),

    CLEAR(
        "clear",
        "Pulisce il terminale",
        () -> Commands.clearCommand()
    ),

    LIST(
        "list",
        "Mostra l'elenco dei comandi disponibili",
        () -> Commands.listCommand()
    ),

    MANUAL(
        "man",
        "Guida all'uso di un comando",
        () -> Commands.manualCommand()
    ),

    VPN(
        "wp-quick",
        "Connettiti alla VPN",
        () -> Commands.vpnCommand()
    ),

    // CHALLENGE
    // SQL INJECTION
    SQLINJECTION(
        "sqlmap",
        "",
        () -> Commands.sqlCommand()
    ),

    // REV/PWN
    PWN(
        "gdb",
        "",
        () -> Commands.pwnCommand()
    );

    private final String command;
    private final String description;
    private final Supplier<String> action;

    EnumCommands(String command, String description, Supplier<String> action) {
        this.command = command;
        this.description = description;
        this.action = action;
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

    public Supplier<String> getAction() {
        return action;
    }

    public String exec() {
        return action.get();
    }

    public static EnumCommands fromString(final String command) {
        return Stream.of(EnumCommands.values())
            .filter(c -> c.getCommand().equals(command))
            .findFirst()
            .orElse(EnumCommands.INVALID);
    }
 }
