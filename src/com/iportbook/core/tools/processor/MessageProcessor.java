package com.iportbook.core.tools.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: theres a lot of repetition => make the code softer
 */
public class MessageProcessor extends ByteProcessor {
    private static final byte[] CONS_PLUS = new byte[]{(byte) '+', (byte) '+', (byte) '+'};
    private static final int SIZE_MAX = 300;
    private static final int SIZE_IP_DIFF = 15;
    private static final int SIZE_NUM_ITEM = 3;
    private static final int SIZE_MESS = 200;
    private static final int SIZE_ID = 8;
    private static final int SIZE_PORT = 4;
    private static final int SIZE_NUM_MESS = 4;
    private static final int SIZE_MDP = 2;
    private static final int SIZE_TYPE = 5;

    public MessageProcessor() {
        super(SIZE_MAX);
    }

    public MessageProcessor(byte[] data) {
        super(data);
    }

    public MessageProcessor(String type) {
        this();
        set(type);
    }

    public MessageProcessor close() {
        return (MessageProcessor) set(CONS_PLUS);
    }

    public String getType() {
        return setOffset(0).getString(SIZE_TYPE);
    }

    public MessageProcessor setType(String value) {
        return (MessageProcessor) setOffset(0).set(value);
    }

    public String getId() {
        return jump().getString(SIZE_ID);
    }

    public MessageProcessor setId(String value) {
        return (MessageProcessor) set(' ').set(value);
    }

    public String getMess() {
        return jump().getString(SIZE_MESS);
    }

    public MessageProcessor setMess(String value) {
        return (MessageProcessor) set(' ').set(value);
    }

    public String getIpDiff() throws Exception {
        String value = jump().getString(SIZE_IP_DIFF);
        if (value.length() != SIZE_IP_DIFF)
            throw new Exception();
        Matcher matcher = Pattern
                .compile("^((\\d{0,3}\\.){3}(\\d{0,3}))#*$", Pattern.DOTALL)
                .matcher(value);
        if (!matcher.find())
            throw new Exception();
        return matcher.group(1);
    }

    public int getNumItem() {
        return jump().getInt(SIZE_NUM_ITEM);
    }

    public MessageProcessor setNumItem(int value) {
        return (MessageProcessor) set(' ').set(value, SIZE_NUM_ITEM);
    }

    public int getNumMess() {
        return jump().getInt(SIZE_NUM_MESS);
    }

    public MessageProcessor setNumMess(int value) {
        return (MessageProcessor) set(' ').set(value, SIZE_NUM_MESS);
    }

    public int getPassword() {
        return jump().getIntByLittleEndian(SIZE_MDP);
    }

    public MessageProcessor setPassword(int value) {
        return (MessageProcessor) set(' ').setIntByLittleEndian(value);
    }

    public int getPort() {
        return jump().getInt(SIZE_PORT);
    }

    public byte[] build() {
        return close().getBytes();
    }
}
