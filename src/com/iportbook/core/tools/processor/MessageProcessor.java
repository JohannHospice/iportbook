package com.iportbook.core.tools.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TCP message
 * TODO: theres a lot of repetition => make the code softer
 */
public class MessageProcessor extends ByteProcessor {
    private static final byte[] CONS_PLUS = new byte[]{(byte) '+', (byte) '+', (byte) '+'};
    private static final byte[] WHITEESCAPE = new byte[]{(byte) ' '};
    private static final int SIZE_MAX = 300;
    private static final int SIZE_IP_DIFF = 15;
    private static final int SIZE_NUM_ITEM = 3;
    private static final int SIZE_MESS = 200;
    private static final int SIZE_ID = 8;
    private static final int SIZE_PORT = 4;
    private static final int SIZE_NUM_MESS = 4;
    private static final int SIZE_MDP = 2;
    private static final int SIZE_TYPE = 5;
    private static final int SIZE_PROM_MESS = 300;

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

    public String getType() {
        return setOffset(0).getString(SIZE_TYPE);
    }

    public String getId() throws Exception {
        return jump().getStringUntil(SIZE_ID, WHITEESCAPE, CONS_PLUS);
    }

    public String getMess() throws Exception {
        return jump().getStringUntil(SIZE_MESS, CONS_PLUS);
    }

    public String getPromMess() throws Exception {
        return jump().getStringUntil(SIZE_PROM_MESS, CONS_PLUS);
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

    public int getNumMess() {
        return jump().getInt(SIZE_NUM_MESS);
    }

    public int getPassword() {
        return jump().getIntByLittleEndian(SIZE_MDP);
    }

    public int getPort() {
        return jump().getInt(SIZE_PORT);
    }

    public int getNumItem() {
        return jump().getInt(SIZE_NUM_ITEM);
    }

    public MessageProcessor setType(String value) {
        return (MessageProcessor) setOffset(0).set(value);
    }

    public MessageProcessor setId(String value) {
        return (MessageProcessor) set(' ').set(value);
    }

    public MessageProcessor setMess(String value) {
        return (MessageProcessor) set(' ').set(value);
    }

    public MessageProcessor setNumItem(int value) {
        return (MessageProcessor) set(' ').set(value, SIZE_NUM_ITEM);
    }

    public MessageProcessor setNumMess(int value) {
        return (MessageProcessor) set(' ').set(value, SIZE_NUM_MESS);
    }

    public MessageProcessor setPassword(int value) {
        return (MessageProcessor) set(' ').setIntByLittleEndian(value);
    }

    public MessageProcessor setIpDiff(String ipDiff) throws Exception {
        Matcher matcher = Pattern.compile("^((\\d{0,3}\\.){3}(\\d{0,3}))$", Pattern.DOTALL).matcher(ipDiff);
        if (!matcher.find())
            throw new Exception();
        set(' ').set(matcher.group(1));
        for (int i = 0; i < SIZE_IP_DIFF - ipDiff.length(); i++)
            set('#');
        return this;
    }

    public MessageProcessor setPort(int port) {
        return (MessageProcessor) set(' ').set(port, SIZE_PORT);
    }

    public MessageProcessor close() {
        return (MessageProcessor) set(CONS_PLUS);
    }

    public byte[] build() {
        return close().getBytes();
    }
}
