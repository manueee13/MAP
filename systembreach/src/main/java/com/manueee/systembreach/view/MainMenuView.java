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

        JButton newGameButton = new JButton("Nuova partita");
        JButton loadGameButton = new JButton("Carica partita");
        JButton optionsButton = new JButton("Opzioni");

        newGameButton.setPreferredSize(buttonSize);
        loadGameButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);

        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(optionsButton);
    
        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());
        optionsButton.addActionListener(e -> openOptions());

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