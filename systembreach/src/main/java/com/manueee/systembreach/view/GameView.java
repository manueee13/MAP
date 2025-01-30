package com.manueee.systembreach.view;

import com.manueee.systembreach.util.fonts.FontUtils;
import com.manueee.systembreach.controller.CommandController;
import com.manueee.systembreach.controller.GameController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * View principale del gioco.
 * Gestisce l'interfaccia grafica della finestra di gioco, inclusi il terminale,
 * il pannello informazioni e il timer.
 */
public class GameView extends JFrame {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 960;
    private static final Color TERMINAL_COLOR = Color.GREEN;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = new Color(80, 80, 80);

    private JPanel gamePanel;
    private JTextArea terminalPanel;
    private JLabel timerLabel;
    private JPanel infoPanel;
    private Font customFont;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JSlider musicSlider;
    private JSlider soundSlider;
    private CommandController commandController;
    private GameController gameController;

    public GameView(CommandController commandController, GameController gameController) {
        setTitle("System Breach");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        customFont = FontUtils.loadFont();
        menuBar();
        
        gamePanel = gamePanel();
        JPanel optionsPanel = optionsPanel();
        
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(optionsPanel, "Options");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "Game");
        setVisible(true);

        this.commandController = commandController;
        this.gameController = gameController;
    }

    private void menuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BACKGROUND_COLOR);
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
        menu.setForeground(TERMINAL_COLOR);
        menu.setBackground(BACKGROUND_COLOR);
        menu.setFont(customFont);
    }

    private void styleMenuItem(JMenuItem menuItem) {
        menuItem.setForeground(TERMINAL_COLOR);
        menuItem.setBackground(BACKGROUND_COLOR);
        menuItem.setFont(customFont);
    }
    /**
     * Pannello di gioco
     * @return
     */
    private JPanel gamePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(BACKGROUND_COLOR);

        Color borderColor = BORDER_COLOR;

        // Pannello sinistro (Terminal)
        terminalPanel();
        JScrollPane terminalScrollPane = new JScrollPane(terminalPanel);
        terminalScrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 3));
        terminalScrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        panel.add(terminalScrollPane, BorderLayout.CENTER);

        // Pannello destro (info + timer)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 5));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createLineBorder(borderColor, 3));

        // Info panel in alto
        infoPanel();
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        infoScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoScrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        rightPanel.add(infoScrollPane, BorderLayout.CENTER);

        // Timer in basso
        timerLabel();
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(BACKGROUND_COLOR);
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
        terminalPanel.setBackground(BACKGROUND_COLOR);
        terminalPanel.setForeground(TERMINAL_COLOR);
        terminalPanel.setCaretColor(TERMINAL_COLOR);
        terminalPanel.setMargin(new Insets(10, 10, 10, 10));
        terminalPanel.setLineWrap(true);
        terminalPanel.setWrapStyleWord(true);

        // Prompt iniziale
        terminalPanel.setText("user@system:~/ $ ");

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
                    // Esegui il comando
                    String output = commandController.processCommand(command);
                    terminalPanel.append(output);
                    // Nuovo prompt
                    terminalPanel.append("\nuser@system:~/ $ ");

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
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
        // Scroll pane principale
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40), 2));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setPreferredSize(new Dimension(300, getHeight()));
    }

    private void timerLabel() {
        timerLabel = new JLabel("30:00");
        timerLabel.setFont(customFont);
        timerLabel.setForeground(TERMINAL_COLOR);
        timerLabel.setBackground(BACKGROUND_COLOR);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timerLabel.setOpaque(true);
    }

    private JPanel optionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel musicLabel = new JLabel("Musica");
        JLabel soundLabel = new JLabel("Sonori");
        musicLabel.setForeground(TERMINAL_COLOR);
        soundLabel.setForeground(TERMINAL_COLOR);

        Dimension sliderSize = new Dimension(300, 50);

        musicSlider = new JSlider(0, 100, 100);
        styleOptionSlider(musicSlider, sliderSize);

        soundSlider = new JSlider(0, 100, 100);
        styleOptionSlider(soundSlider, sliderSize);

        JButton backButton = new JButton("Indietro");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setForeground(TERMINAL_COLOR);
        backButton.setBackground(BACKGROUND_COLOR);
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
        slider.setForeground(TERMINAL_COLOR);
        slider.setBackground(BACKGROUND_COLOR);
    }

    public void updateTimer(String time) {
        SwingUtilities.invokeLater(() -> timerLabel.setText(time));
    }

    /**
     * Aggiunge una nuova mail al pannello informazioni
     * @param mailId ID univoco della mail
     * @param sender Mittente della mail
     * @param object Oggetto della mail
     */
    public void addMailEntry(int mailId, String sender, String object) {
        SwingUtilities.invokeLater(() -> {
            JPanel mailPanel = createMailPanel(sender, object);
            mailPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    gameController.viewMail(mailId);
                }
            });
            infoPanel.add(mailPanel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.revalidate();
            infoPanel.repaint();
        });
    }

    /**
     * Crea un pannello per visualizzare una mail nella lista
     * @param sender Mittente della mail
     * @param object Oggetto della mail
     * @return JPanel configurato per la visualizzazione della mail
     */
    private JPanel createMailPanel(String sender, String object) {
        JPanel mailPanel = new JPanel();
        mailPanel.setLayout(new BoxLayout(mailPanel, BoxLayout.Y_AXIS));
        mailPanel.setBackground(BACKGROUND_COLOR);
        mailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        JLabel senderLabel = new JLabel("> " + sender);
        senderLabel.setFont(customFont.deriveFont(24f));
        senderLabel.setForeground(TERMINAL_COLOR);
        senderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel objectLabel = new JLabel(object);
        objectLabel.setFont(customFont.deriveFont(24f));
        objectLabel.setForeground(TERMINAL_COLOR);
        objectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mailPanel.add(senderLabel);
        mailPanel.add(Box.createVerticalStrut(2));
        mailPanel.add(objectLabel);
        
        return mailPanel;
    }

    public void showGameOverDialog() {
        JOptionPane.showMessageDialog(this, "Hai perso! La centrale è esplosa.", "Game Over", JOptionPane.ERROR_MESSAGE);
    }

    public void addStartTimerListener(ActionListener listener) {
        // Implementa la logica per aggiungere un listener al timer
    }

    /**
     * Salva la sessione corrente
     */
    private void saveSession() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva sessione");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("File di salvataggio (.mntcrl)", "mntcrl");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".mntcrl")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".mntcrl");
            }

            if(selectedFile.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "Il file esiste già. Vuoi sovrascriverlo?", "Conferma sovrascrittura", JOptionPane.YES_NO_OPTION);
                if(result != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            gameController.saveSession(selectedFile);
        }
    }

    /**
     * Carica una sessione salvata
     */
    private void loadSession() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carica sessione");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File di salvataggio (.mntcrl)", "mntcrl");
        fileChooser.setFileFilter(filter);
        
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            dispose(); // Chiude la finestra corrente
            new GameController(false, selectedFile); // Crea nuovo controller con sessione caricata
        }
    }
}
