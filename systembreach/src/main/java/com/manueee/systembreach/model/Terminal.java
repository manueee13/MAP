package com.manueee.systembreach.model;

import java.util.List;

public class Terminal {
    private List<String> commandHistory;
    private String currentDirectory;
    private StringBuilder outputBuffer;

    public Terminal() {
        //this.commandHistory = new ArrayList<>();
        this.currentDirectory = "~/";
        this.outputBuffer = new StringBuilder();
    }

    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    public void appendOutput(String output) {
        outputBuffer.append(output).append("\n");
    }

    public String getPrompt() {
        return "user@system:" + currentDirectory + "$ ";
    }
    
    
}
