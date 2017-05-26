package com.iportbook.core.tools.composed;

import java.util.ArrayList;

public class ComposedText {

    public int size;
    public ArrayList<String> message = new ArrayList<>();

    public ComposedText(int num) {
        this.size = num;
    }

    public String pack() {
        StringBuilder cct = new StringBuilder();
        for (String str : message) {
            cct.append(message);
        }
        return cct.toString();
    }
}
