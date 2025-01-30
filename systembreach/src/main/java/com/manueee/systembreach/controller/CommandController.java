package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.util.commands.Commands;
import com.manueee.systembreach.util.commands.EnumCommands;

/**
 * Controller che gestisce l'elaborazione dei comandi inseriti dall'utente.
 * Si occupa di validare i comandi, verificarne la disponibilità e delegarne l'esecuzione.
 */
public class CommandController {
    private final Terminal terminal;
    private final GameState gameState;

    /**
     * Crea un nuovo controller dei comandi
     * @param terminal il terminale su cui operare
     * @param gameState lo stato del gioco corrente
     * @throws IllegalArgumentException se terminal o gameState sono null
     */
    public CommandController(Terminal terminal, GameState gameState) {
        if (terminal == null || gameState == null) {
            throw new IllegalArgumentException("Terminal and GameState cannot be null");
        }
        this.terminal = terminal;
        this.gameState = gameState;
    }

    /**
     * Elabora un comando inserito dall'utente
     * @param input il comando da elaborare
     * @return il risultato dell'elaborazione del comando
     */
    public String processCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Commands.invalidCommand();
        }

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        EnumCommands cmd = EnumCommands.fromString(command);
        if (!gameState.isCommandAvailable(cmd)) {
            return Commands.invalidCommand();
        }

        String result = executeCommand(cmd, args);
        updateTerminal(command, result);
        return result;
    }

    /**
     * Esegue il comando specificato
     * @param cmd il comando da eseguire
     * @param args gli argomenti del comando
     * @return il risultato dell'esecuzione
     */
    private String executeCommand(EnumCommands cmd, String args) {
        String result;
        switch (cmd) {
            case LIST:
                result = Commands.listCommand(gameState);
                break;
            case LS:
                result = Commands.lsCommand(gameState.getFileSystem(), args);
                break;
            case CD:
                result = Commands.cdCommand(gameState.getFileSystem(), args);
                break;
            case CAT:
                result = Commands.catCommand(gameState, args);
                break;
            case CLEAR:
                Commands.clearCommand(terminal);
                result = "";
                break;
            case MANUAL:
                result = Commands.manualCommand(args);
                break;
            case DECRYPT:
                result = Commands.decryptCommand(gameState, args);
                break;
            case CURL:
                result = Commands.curlCommand(gameState, args);
                break;
            case SQLINJECT:
                result = Commands.sqlinjectCommand(args);
                break;
            default:
                result = Commands.invalidCommand();
                break;
        }
        return result;
    }

    /**
     * Aggiorna lo stato del terminale
     * @param command il comando eseguito
     * @param result il risultato dell'esecuzione
     */
    private void updateTerminal(String command, String result) {
        terminal.addCommand(command);
        if (!result.isEmpty()) {
            terminal.addOutput(result);
        }
    }
}
