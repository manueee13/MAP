package com.manueee.systembreach.model;

import java.util.List;
import java.util.ArrayList;

/**
 * <h2>Terminal</h2>
 * Classe <b>model</b> per la gestione del terminale.
 */
public class Terminal {
    private List<String> commandHistory;
    private List<String> outputBuffer;

    public Terminal() {
        this.commandHistory = new ArrayList<>();
        this.outputBuffer = new ArrayList<>();
    }

    public void addCommand(String command) {
        commandHistory.add(command);
    }

    public void addOutput(String output) {
        outputBuffer.add(output);
    }

    public List<String> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    public List<String> getOutputBuffer() {
        return new ArrayList<>(outputBuffer);
    }

    public void clearOutputBuffer() {
        outputBuffer.clear();
    }
    
}
