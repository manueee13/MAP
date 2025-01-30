package com.manueee.systembreach.util.commands;

import java.util.stream.Stream;

/**
 * Enumerazione dei comandi disponibili nel gioco.
 * Parte del layer Controller nel pattern MVC, gestisce le definizioni dei comandi.
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

    DECRYPT(
        "fcrackzip",
        "frackzip version 1.0, a fast/free zip password cracker.\n\nUSAGE: fcrackzip <args> <path>\n\t[-b] - use brute-force algorithm\n\t[-D] <path> - use a dictionary\n\n"
    ),

    CURL(
        "curl",
        "USAGE: curl\n\t[-u] <url> - URL to fetch\n\t[-o] - Enable download mode\n\t[-d] <data> (example: -d param1=var1&param2=var2)- Send data in POST request\n\n"
    ),

    SQLINJECT(
        "sqlmap",
        "sqlmap {1.2.11#stable}, a SQL injection tool.\n\nUSAGE: sqlmap\n\t[-u] <url> - URL to test\n\t[--dbs] - Enumerate databases\n\t[-D] <database> - Target database\n\t[--tables] - Enumerate tables\n\t[-T] <table> - Target table\n\t[--dump] - Dump the table\n\n"
    ),

    REVERSE(
        "objdump",
        "USAGE: objdump\n\t[-d] <file> - Disassemble <file>\n\t[-s] <file> - Display sections of <file>\n\t[-t] <file> - Display symbol table of <file>\n\n"
    );

    private static final String NEWLINE = "\n";
    
    private final String command;
    private final String description;

    EnumCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    /**
     * @return L'identificatore del comando
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return La descrizione e l'uso del comando
     */
    public String getDescription() {
        return description;
    }

    /**
     * Ottiene le informazioni formattate del comando
     * @return Descrizione del comando con newline
     */
    public String getCommandInfo() {
        return description + NEWLINE;
    }

    /**
     * Converte una stringa in un comando enum
     * @param command Stringa del comando da convertire
     * @return Comando corrispondente o INVALID se non trovato
     */
    public static EnumCommands fromString(final String command) {
        return Stream.of(EnumCommands.values())
            .filter(c -> c.getCommand().equals(command))
            .findFirst()
            .orElse(EnumCommands.INVALID);
    }
}
