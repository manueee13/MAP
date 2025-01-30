package com.manueee.systembreach.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe model che rappresenta il terminale di gioco.
 * Gestisce la cronologia dei comandi e il buffer di output.
 */
public class Terminal {
    private final List<String> commandHistory;
    private final List<String> outputBuffer;

    /**
     * Costruisce un nuovo terminale con buffer vuoti
     */
    public Terminal() {
        this.commandHistory = new ArrayList<>();
        this.outputBuffer = new ArrayList<>();
    }

    /**
     * Aggiunge un comando alla cronologia
     * @param command Il comando da aggiungere
     * @throws IllegalArgumentException se il comando è null
     */
    public void addCommand(String command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        commandHistory.add(command);
    }

    /**
     * Aggiunge un output al buffer
     * @param output L'output da aggiungere
     */
    public void addOutput(String output) {
        outputBuffer.add(output);
    }

    /**
     * Restituisce la cronologia dei comandi
     * @return Una lista della cronologia dei comandi
     */
    public List<String> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    /**
     * Restituisce il buffer di output
     * @return Una lista del buffer di output
     */
    public List<String> getOutputBuffer() {
        return new ArrayList<>(outputBuffer);
    }

    /**
     * Pulisce il buffer di output
     */
    public void clearOutputBuffer() {
        outputBuffer.clear();
    }
    
}
