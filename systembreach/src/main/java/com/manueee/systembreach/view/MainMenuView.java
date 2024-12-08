package com.manueee.systembreach.view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame{
    public MainMenuView() 
    {

        /*
        * Impostazioni finestra principale
        */
        setTitle("System Breach");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        /*
        * Layout pulsanti
        */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        Dimension buttonSize = new Dimension(200, 50);

        String[] buttonLabels = {"Nuova partita", "Carica partita", "Opzioni"};
        Runnable[] buttonActions = {
            this::startNewGame,
            this::loadGame,
            this::openOptions
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            final int index = i; // Necessario per lambda expressions
            button.addActionListener(e -> buttonActions[index].run());
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(button);
        }

        buttonPanel.add(Box.createVerticalGlue());
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startNewGame() {
        //TODO
        JOptionPane.showMessageDialog(this, "pre-build 0.1");
    }

    private void loadGame() {
        //TODO
        JOptionPane.showMessageDialog(this, "Funzione di caricamento non ancora implementata.");
    }

    private void openOptions() {
        //TODO
        JOptionPane.showMessageDialog(this, "Schermata opzioni non ancora implementata.");
    }
}