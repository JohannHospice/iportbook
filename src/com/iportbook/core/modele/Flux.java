package com.iportbook.core.modele;

import com.iportbook.core.tools.message.MessageTCP;

import java.io.Serializable;
import java.util.ArrayList;

public class Flux implements Serializable {
    private ArrayList<MessageTCP> partials = new ArrayList<>();
    private MessageTCP message;
    private int type;

    public Flux() {
    }

    public Flux(int type) {
        this.type = type;
    }

    public Flux(MessageTCP message) {
        this.message = message;
    }

    public Flux(int type, MessageTCP message) {
        this(type);
        this.message = message;
    }

    public MessageTCP getMessage() {
        return message;
    }

    public Flux setMessage(MessageTCP message) {
        this.message = message;
        return this;
    }

    public Flux addPartial(MessageTCP message) {
        partials.add(message);
        return this;
    }

    public int getPartialSize() {
        return partials.size();
    }

    public MessageTCP getPartial(int i) {
        return partials.get(i);
    }

    public int getType() {
        return type;
    }

    public Flux setType(int type) {
        this.type = type;
        return this;
    }

    public boolean hasPartials() {
        return getPartialSize() > 0;
    }
}
