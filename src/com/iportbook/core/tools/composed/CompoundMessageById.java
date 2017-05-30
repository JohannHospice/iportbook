package com.iportbook.core.tools.composed;

public class CompoundMessageById extends CompoundMessage {
    private String id;

    public CompoundMessageById(String id, int num) {
        super(num);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
