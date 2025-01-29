package com.manueee.systembreach.controller;

import com.manueee.systembreach.model.GameState;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.util.commands.Commands;
import com.manueee.systembreach.util.commands.EnumCommands;

/**
 * <h2>CommandController</h2>
 * Classe <b>controller</b> per l'interazione dei comandi in input dell'utente.
 */
public class CommandController {
    private final Terminal terminal;
    private final GameState gameState;

    public CommandController(Terminal terminal, GameState gameState) {
        this.terminal = terminal;
        this.gameState = gameState;
    }

    /**
     * <code>processCommand</code>
     * Processa i comandi forniti in input dall'utente, verificando che il
     * comando sia disponibile nella sessione e che sia corretto.
     * @param input della stringa di comando fornita dall'utente
     * @return <code>invalidCommand()</code> se il comando non è valido o non disponibile
     */
    public String processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        EnumCommands cmd = EnumCommands.fromString(command);
        if (!gameState.isCommandAvailable(cmd)) {
            return Commands.invalidCommand();
        }

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
        }
        terminal.addCommand(command);
        terminal.addOutput(result);
        return result;
    }
}
