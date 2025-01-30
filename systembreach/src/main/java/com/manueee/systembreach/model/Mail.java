package com.manueee.systembreach.model;

/**
 * Classe model che rappresenta un messaggio di posta elettronica nel gioco.
 */
public class Mail {
    private final String sender;
    private final String object;
    private final String content;

    /**
     * Costruisce un nuovo messaggio email
     * @param sender Il mittente del messaggio
     * @param object L'oggetto del messaggio
     * @param content Il contenuto del messaggio
     * @throws IllegalArgumentException se qualsiasi parametro è null
     */
    public Mail(String sender, String object, String content) {
        if (sender == null || object == null || content == null) {
            throw new IllegalArgumentException("Mail parameters cannot be null");
        }
        this.sender = sender;
        this.object = object;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getObject() {
        return object;
    }

    public String getContent() {
        return content;
    }
}
