package com.iportbook.core.tools;

public class Tools {
    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    public static int byteArrayToInt(byte[] values) {
        return values[3] & 0xFF |
                (values[2] & 0xFF) << 8 |
                (values[1] & 0xFF) << 16 |
                (values[0] & 0xFF) << 24;
    }
}
