package com.manueee.systembreach.view;

import javax.swing.*;
import java.awt.*;

/**
 * <h2>HelpDialog</h2>
 * Dialog che mostra l'help del gioco.
 * Visualizza una lista di comandi disponibili e le relative descrizioni.
 */
public class HelpDialog extends JDialog {
    private static final int DIALOG_WIDTH = 660;
    private static final int DIALOG_HEIGHT = 610;
    private static final int FONT_SIZE = 20;
    
    public HelpDialog(Frame parent) {
        super(parent, "Aiuto", true);
        initializeDialog();
        setupHelpContent();
    }

    private void initializeDialog() {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(Color.BLACK);
    }

    private void setupHelpContent() {
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setBackground(Color.BLACK);
        helpText.setForeground(Color.GREEN);
        helpText.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE));
        
        StringBuilder text = new StringBuilder();
        text.append("╔════════════════════════════╗\n");
        text.append("║                COMANDI SISTEMA               ║\n");
        text.append("╚════════════════════════════╝\n");
        text.append("\n");
        text.append(String.format("%-15s%-35s\n", "COMANDO", "DESCRIZIONE"));
        text.append(String.format("%-30s\n", "──────────────────────────────"));
        text.append("\n");
        text.append(String.format("%-15s%-35s\n", "cd <dir>", "Cambia directory corrente"));
        text.append(String.format("%-15s%-35s\n", "ls", "Lista directory corrente"));
        text.append(String.format("%-15s%-35s\n", "cat <file>", "Leggi contenuto file"));
        text.append(String.format("%-15s%-35s\n", "man <command>", "Manuale del comando"));
        text.append(String.format("%-15s%-35s\n", "list", "Lista comandi"));
        text.append(String.format("%-15s%-35s\n", "clear", "Pulisce terminale"));
        text.append("\n");
        text.append(String.format("%-30s\n", "──────────────────────────────"));
        text.append("\n");
        text.append(String.format("%-50s\n", "Tip: Usa 'man <command>' per ottenere informazioni"));
        text.append(String.format("%-50s\n", "    dettagliate sul comando specifico."));
        
        helpText.setText(text.toString());
        helpText.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        add(scrollPane);
    }
}
