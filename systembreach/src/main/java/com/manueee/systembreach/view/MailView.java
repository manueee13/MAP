package com.manueee.systembreach.view;

import javax.swing.*;
import java.awt.*;

public class MailView extends JDialog{

    public MailView(Frame parent, String sender, String object, String content) {
        super(parent, "Mail", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JTextArea mailText = new JTextArea();
        mailText.setEditable(false);
        mailText.setBackground(Color.BLACK);
        mailText.setForeground(Color.GREEN);
        mailText.setFont(new Font("Monospaced", Font.PLAIN, 20));
        mailText.setMargin(new Insets(20, 20, 20, 20));
        mailText.setLineWrap(true);
        mailText.setWrapStyleWord(true);

        StringBuilder text = new StringBuilder();
        text.append("✉ MITTENTE: " + sender + "\n");
        text.append("OGGETTO: " + object + "\n\n");
        text.append(content);

        mailText.setText(text.toString());

        JScrollPane scrollPane = new JScrollPane(mailText);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);

        JButton closeButton = new JButton("Chiudi");
        closeButton.setBackground(Color.BLACK);
        closeButton.setForeground(Color.GREEN);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
