package com.iportbook.core.modele;

import com.iportbook.core.tools.message.MessageTCP;

import java.util.ArrayList;

public class Flux {
    private ArrayList<MessageTCP> messages = new ArrayList<>();
    private String content;
    private String idFrom;
    private int type;

    public Flux(int type) {
        this.type = type;
    }

    public Flux(int type, String from) {
        this.type = type;
        this.idFrom = from;
    }

    public int getMessagesSize() {
        return messages.size();
    }

    public MessageTCP popMessage() {
        MessageTCP message = messages.get(getMessagesSize());
        removeMessage(message);
        return message;
    }

    public MessageTCP getMessage(int i) {
        return messages.get(i);
    }

    public void addMessage(MessageTCP message) {
        messages.add(message);
    }

    public void removeMessage(MessageTCP message) {
        messages.remove(message);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }
}
