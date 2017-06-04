package com.iportbook.core.modele;


import java.io.Serializable;
import java.util.ArrayList;

public class Flux implements Serializable {

    public static final int FRIE = 0;
    public static final int OKIRF = 1;
    public static final int NOKRF = 2;
    public static final int MESS = 3;
    public static final int FLOO = 4;
    public static final int PUBL = 5;

    private ArrayList<byte[]> partials = new ArrayList<>();
    private byte[] message;
    private byte type;

    public Flux(byte[] message) {
        this.message = message;
    }

    public Flux(byte type) {
        this.type = type;
    }

    public Flux(byte type, byte[] message) {
        this(type);
        this.message = message;
    }

    public Flux(int type) {
        this((byte) type);
    }

    public Flux(int type, byte[] message) {
        this((byte) type, message);
    }

    public byte[] getMessage() {
        return message;
    }

    public Flux setMessage(byte[] message) {
        this.message = message;
        return this;
    }

    public Flux addPartial(byte[] message) {
        partials.add(message);
        return this;
    }

    public int getPartialSize() {
        return partials.size();
    }

    public byte[] getPartial(int i) {
        return partials.get(i);
    }

    public byte getType() {
        return type;
    }

    public Flux setType(byte type) {
        this.type = type;
        return this;
    }

    public Flux setType(int type) {
        return setType((byte) type);
    }

    public boolean hasPartials() {
        return getPartialSize() > 0;
    }
}
