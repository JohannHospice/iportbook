package com.iportbook.core.tools.processor;

import com.iportbook.core.tools.Utility;

import java.util.Arrays;
import java.util.logging.Logger;

public class ByteProcessor {
    private final byte[] data;
    private int offset = 0;
    private int size = 0;

    public ByteProcessor(byte[] data) {
        Logger.getGlobal().info(new String(data, 0, data.length));
        this.data = data;
    }

    public ByteProcessor(int length) {
        data = new byte[length];
    }

    public ByteProcessor jump() {
        offset++;
        return this;
    }

    public ByteProcessor setData(byte[] data, int offset, int length) {
        System.arraycopy(data, offset, this.data, this.offset, length);
        this.offset += length;
        return this;
    }

    public ByteProcessor set(byte value) {
        data[offset] = value;
        offset++;
        return this;
    }

    public ByteProcessor set(char value) {
        data[offset] = (byte) value;
        offset++;
        return this;
    }

    public ByteProcessor set(byte[] value) {
        System.arraycopy(value, 0, data, offset, value.length);
        offset += value.length;
        return this;
    }

    public ByteProcessor set(String str) {
        set(str.getBytes());
        return this;
    }

    public ByteProcessor set(int value, int length) {
        StringBuilder str = new StringBuilder(String.valueOf(value));
        for (int i = str.length(); i < length; i++)
            str.insert(0, '0');
        return set(str.toString());
    }

    public ByteProcessor setIntByLittleEndian(int value) {
        byte[] lit = Utility.intToByteArray(value);
        return set(lit[0]).set(lit[1]);
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public ByteProcessor setOffset(int offset) {
        if (offset > this.offset)
            size = offset;
        this.offset = offset;
        return this;
    }

    public int getInt(int length) {
        return Integer.parseInt(getString(length));
    }

    public int getInt() {
        int value = data[offset];
        offset++;
        return value;
    }

    public int getIntByLittleEndian(int length) {
        int value = Utility.littleEndianToInt(data, offset, length);
        offset += length;
        return value;
    }

    public String getString(int length) {
        String value = new String(data, offset, length);
        offset += length;
        return value;
    }

    public String getStringUntil(int expect) throws Exception {
        StringBuilder str = new StringBuilder();
        int i = offset;
        while (i < data.length && i - offset < expect)
            str.append((char) data[i++]);
        offset += str.length();
        return str.toString();
    }

    public String getStringUntil(int expect, byte until) throws Exception {
        StringBuilder str = new StringBuilder();
        int i = offset;
        while (i < data.length) {
            if (until == data[i])
                break;
            str.append((char) data[i++]);
        }
        if (str.length() >= expect)
            throw new Exception();
        offset += str.length();
        return str.toString();
    }

    public String getStringUntil(int limit, byte[]... until) throws Exception {
        StringBuilder str = new StringBuilder();
        byte[][] tmp = new byte[until.length][];
        for (int i = 0; i < until.length; i++)
            tmp[i] = new byte[until[i].length];
        int i = offset;
        boolean run = true;
        while (i < data.length && i - offset < limit && run) {
            for (int j = 0; j < until.length; j++)
                Utility.dequeue(tmp[j], data[i]);
            str.append((char) data[i++]);
            for (int j = 0; j < until.length; j++) {
                if (Arrays.equals(tmp[j], until[j])) {
                    str.setLength(str.length() - until[j].length);
                    run = false;
                    break;
                }
            }
        }
        if (str.length() > limit)
            throw new Exception();
        offset += str.length();
        return str.toString();
    }

    public byte[] getBytes() {
        byte[] res = new byte[offset];
        System.arraycopy(data, 0, res, 0, offset);
        Logger.getGlobal().info(new String(res, 0, offset));
        return res;
    }
}
