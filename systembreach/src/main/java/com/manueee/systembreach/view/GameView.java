package com.manueee.systembreach.view;

import com.manueee.systembreach.util.fonts.FontUtils;
import com.manueee.systembreach.controller.CommandController;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Classe GameView
 * Classe che gestisce la finestra di gioco principale
 */
public class GameView extends JFrame {
    private JPanel gamePanel;
    private JTextArea terminalPanel;
    private JLabel timerLabel;
    private JTextArea infoPanel;
    private Font customFont;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JSlider musicSlider;
    private JSlider soundSlider;

    public GameView() {
        setTitle("System Breach");
        setSize(1280, 960);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.BLACK);

        customFont = FontUtils.loadFont();
        menuBar();
        
        gamePanel = gamePanel();
        JPanel optionsPanel = optionsPanel();
        
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(optionsPanel, "Options");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "Game");
        setVisible(true);
    }

    private void menuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));

        JMenu fileMenu = new JMenu("File");
        styleMenu(fileMenu);
        
        JMenuItem saveItem = new JMenuItem("Salva sessione");
        JMenuItem loadItem = new JMenuItem("Carica sessione");
        JMenuItem exitItem = new JMenuItem("Esci");
        
        styleMenuItem(saveItem);
        styleMenuItem(loadItem);
        styleMenuItem(exitItem);

        saveItem.addActionListener(e -> saveSession());
        loadItem.addActionListener(e -> loadSession());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu optionsMenu = new JMenu("Opzioni");
        styleMenu(optionsMenu);
        optionsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "Options");
            }
        });

        JMenu helpMenu = new JMenu("Help");
        styleMenu(helpMenu);
        helpMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new HelpDialog(GameView.this).setVisible(true);
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void styleMenu(JMenu menu) {
        menu.setForeground(Color.GREEN);
        menu.setBackground(Color.BLACK);
        menu.setFont(customFont);
    }

    private void styleMenuItem(JMenuItem menuItem) {
        menuItem.setForeground(Color.GREEN);
        menuItem.setBackground(Color.BLACK);
        menuItem.setFont(customFont);
    }
    /**
     * Pannello di gioco
     * @return
     */
    private JPanel gamePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.BLACK);

        Color borderColor = new Color(80, 80, 80);

        // Pannello sinistro (Terminal)
        terminalPanel();
        JScrollPane terminalScrollPane = new JScrollPane(terminalPanel);
        terminalScrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 3));
        terminalScrollPane.getViewport().setBackground(Color.BLACK);
        panel.add(terminalScrollPane, BorderLayout.CENTER);

        // Pannello destro (info + timer)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 5));
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setBorder(BorderFactory.createLineBorder(borderColor, 3));

        // Info panel in alto
        infoPanel();
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        infoScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoScrollPane.getViewport().setBackground(Color.BLACK);
        rightPanel.add(infoScrollPane, BorderLayout.CENTER);

        // Timer in basso
        timerLabel();
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(Color.BLACK);
        timerPanel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        rightPanel.add(timerPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);
        rightPanel.setPreferredSize(new Dimension(getWidth() / 3, getHeight()));

        return panel;
    }

    private void terminalPanel() {
        terminalPanel = new JTextArea();
        terminalPanel.setFont(customFont);
        terminalPanel.setBackground(Color.BLACK);
        terminalPanel.setForeground(Color.GREEN);
        terminalPanel.setCaretColor(Color.GREEN);
        terminalPanel.setMargin(new Insets(10, 10, 10, 10));

        // Prompt iniziale
        terminalPanel.setText("user@system:~$ ");

        terminalPanel.addKeyListener(new KeyListener() {
            private StringBuilder commandBuffer = new StringBuilder();
            private int lastPromptPosition = terminalPanel.getText().length();

            @Override
            public void keyTyped(KeyEvent e) {
                // Impedisci la cancellazione del prompt
                if (terminalPanel.getCaretPosition() < lastPromptPosition) {
                    e.consume();
                    terminalPanel.setCaretPosition(terminalPanel.getText().length());
                    return;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String command = commandBuffer.toString().trim();

                    // Aggiungi nuova riga
                    terminalPanel.append("\n");

                    // Esegui il comando tramite CommandController
                    // TODO
                    // CommandController.executeCommand(command);
                    // Output del comando
                    // TODO
                    // terminalPanel.append(output);

                    // Nuovo prompt
                    terminalPanel.append("\nuser@system:~$ ");

                    // Reset del buffer
                    commandBuffer.setLength(0);
                    lastPromptPosition = terminalPanel.getText().length();
                }
                // Gestione backspace
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (terminalPanel.getCaretPosition() <= lastPromptPosition) {
                        e.consume();
                    }
                    else if (commandBuffer.length() > 0) {
                        commandBuffer.setLength(commandBuffer.length() - 1);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Aggiorna il comando corrente con il testo dopo il prompt
                if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    String text = terminalPanel.getText();
                    commandBuffer = new StringBuilder(text.substring(lastPromptPosition));
                }
            }
        });
    }

    private void infoPanel() {
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setFont(customFont);
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setForeground(Color.GREEN);
        infoPanel.setCaretColor(Color.GREEN);
        infoPanel.setMargin(new Insets(10, 10, 10, 10));
        infoPanel.setText("Lorem ipsum dolor sit amet.");
    }

    private void timerLabel() {
        timerLabel = new JLabel("30:00");
        timerLabel.setFont(customFont);
        timerLabel.setForeground(Color.GREEN);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timerLabel.setOpaque(true);
    }

    private JPanel optionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel musicLabel = new JLabel("Musica");
        JLabel soundLabel = new JLabel("Sonori");
        musicLabel.setForeground(Color.GREEN);
        soundLabel.setForeground(Color.GREEN);

        Dimension sliderSize = new Dimension(300, 50);

        musicSlider = new JSlider(0, 100, 100);
        styleOptionSlider(musicSlider, sliderSize);

        soundSlider = new JSlider(0, 100, 100);
        styleOptionSlider(soundSlider, sliderSize);

        JButton backButton = new JButton("Indietro");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setForeground(Color.GREEN);
        backButton.setBackground(Color.BLACK);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Game"));

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

    private void styleOptionSlider(JSlider slider, Dimension size) {
        slider.setMajorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMaximumSize(size);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setForeground(Color.GREEN);
        slider.setBackground(Color.BLACK);
    }

    /**
     * @return Il pannello del terminale
     */
    public JTextArea getTerminalPanel() {
        return terminalPanel;
    }

    /**
     * @return Il pannello delle informazioni
     */
    public JTextArea getInfoPanel() {
        return infoPanel;
    }

    /**
     * @return L'etichetta del timer
     */
    public JLabel getTimerLabel() {
        return timerLabel;
    }

    public void updateTimer(String time) {
        SwingUtilities.invokeLater(() -> timerLabel.setText(time));
    }

    public void addStartTimerListener(ActionListener listener) {
        // Implementa la logica per aggiungere un listener al timer
    }

    /**
     * Salva la sessione corrente
     */
    private void saveSession() {
        // Implementa la logica per salvare la sessione
    }

    /**
     * Carica una sessione salvata
     */
    private void loadSession() {
        // Implementa la logica per caricare una sessione salvata
    }
}
