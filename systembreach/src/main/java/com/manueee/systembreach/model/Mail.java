package com.manueee.systembreach.model;

//TODO: Implementare classe Mail

public class Mail {
    private String sender;
    private String object;
    private String content;

    public Mail(String sender, String object, String content) {
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
