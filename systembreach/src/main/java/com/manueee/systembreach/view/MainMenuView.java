package com.manueee.systembreach.view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe MainMenuView
 * Classe che gestisce la finestra del menu principale
 */
public class MainMenuView extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JSlider musicSlider;
    private JSlider soundSlider;


    /**
     * Gestione della finestra MainMenu
     */
    public MainMenuView() 
    {
        /*
        * Impostazioni finestra principale
        */
        setTitle("System Breach - Menu principale");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = menuPanel();
        JPanel optionsPanel = optionPanel();

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(optionsPanel, "Opzioni");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel menuPanel() {
        /*
        * Layout pulsanti
        */
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Dimension buttonSize = new Dimension(200, 50);

        JButton newGameButton = new JButton("Nuova partita");
        JButton loadGameButton = new JButton("Carica partita");
        JButton optionsButton = new JButton("Opzioni");

        newGameButton.setPreferredSize(buttonSize);
        newGameButton.setMinimumSize(buttonSize);
        newGameButton.setMaximumSize(buttonSize);

        loadGameButton.setPreferredSize(buttonSize);
        loadGameButton.setMinimumSize(buttonSize);
        loadGameButton.setMaximumSize(buttonSize);

        optionsButton.setPreferredSize(buttonSize);
        optionsButton.setMinimumSize(buttonSize);
        optionsButton.setMaximumSize(buttonSize);
        
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(newGameButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loadGameButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(optionsButton);
        panel.add(Box.createVerticalGlue());
    
        /*
         * Eventi pulsanti
         */
        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());
        optionsButton.addActionListener(e -> cardLayout.show(mainPanel, "Opzioni"));

        add(panel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel optionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel musicLabel = new JLabel("Musica");
        JLabel soundLabel = new JLabel("Sonori");

        Dimension sliderSize = new Dimension(300, 50);

        musicSlider = new JSlider(0, 100, 100);
        musicSlider.setMajorTickSpacing(25);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setMaximumSize(sliderSize);
        musicSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        soundSlider = new JSlider(0, 100, 100);
        soundSlider.setMajorTickSpacing(25);
        soundSlider.setPaintTicks(true);
        soundSlider.setPaintLabels(true);
        soundSlider.setMaximumSize(sliderSize);
        soundSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("Indietro");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        musicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(musicLabel);
        panel.add(musicSlider);
        panel.add(Box.createVerticalStrut(20));
        panel.add(soundLabel);
        panel.add(soundSlider);
        panel.add(Box.createVerticalStrut(20));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void startNewGame() {
        JOptionPane.showMessageDialog(this, "build alpha 0.1\nQuesto è una demo.\nMolte funzionalità non sono ancora state implementate\ne veranno introdotte con una prossima build.");
        dispose();
        new GameView();
    }

    private void loadGame() {
        //TODO: Implementare caricamento partita
        JOptionPane.showMessageDialog(this, "Funzione di caricamento non ancora implementata.");
    }
}