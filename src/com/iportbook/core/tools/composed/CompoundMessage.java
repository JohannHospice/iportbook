package com.iportbook.core.tools.composed;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CompoundMessage {
    private ArrayList<String> message = new ArrayList<>();
    private int size;

    public CompoundMessage(int size) {
        this.size = size;
    }

    public String pack() {
        return message.stream().collect(Collectors.joining());
    }

    public void setMessage(int num, String mess) {
        message.set(num, mess);
    }

    public String getMessage(int num) {
        return message.get(num);
    }

    public int getSize() {
        return size;
    }
}
