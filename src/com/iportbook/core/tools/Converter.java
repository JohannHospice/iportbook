package com.iportbook.core.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Converter {
    public static byte[] intToByteArray(int value) {
        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value);
        bb.flip();
        return bb.array();
    }

    public static int littleEndianToInt(byte[] data, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(4).put(data, offset, length).order(ByteOrder.LITTLE_ENDIAN);
        bb.flip();
        bb.limit(4);
        return bb.getInt();
    }

    public static int littleEndianToInt(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
}
