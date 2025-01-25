package com.manueee.systembreach.view;

import javax.swing.*;
import java.awt.*;

/**
 * <h2>HelpDialog</h2>
 * Classe <b>view</b> per la finestra di aiuto.
 */
public class HelpDialog extends JDialog {
    public HelpDialog(Frame parent) {
        super(parent, "Aiuto", true);
        setSize(660, 610);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.BLACK);

        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setBackground(Color.BLACK);
        helpText.setForeground(Color.GREEN);
        helpText.setFont(new Font("Monospaced", Font.PLAIN, 20));
        
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
