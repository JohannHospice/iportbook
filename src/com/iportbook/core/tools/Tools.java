package com.iportbook.core.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Tools {
    public static byte[] intToByteArray(int value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(value);
        bb.flip();
        return bb.array();
    }

    public static int byteArrayToInt(byte[] data) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data);
        bb.flip();
        return bb.getInt();
    }
}
