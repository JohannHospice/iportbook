package com.iportbook.core.tools.message;

import com.iportbook.core.tools.Tools;

import javax.tools.Tool;

public class MessageUDP {

    private int notificationCode;
    private int fluxSize;

    public MessageUDP(int notificationCode) {
        this.notificationCode = notificationCode;
    }

    public MessageUDP(int notificationCode, int fluxSize) {
        this(notificationCode);
        setFluxSize(fluxSize);
    }

    public MessageUDP(byte[] data) {
        notificationCode = (int) data[0];
        fluxSize = Tools.byteArrayToInt(new byte[]{data[1], data[2], 0, 0});
    }

    public byte[] getBytes() {
        byte[] data = Tools.intToByteArray(fluxSize);
        return new byte[]{(byte) notificationCode, data[0], data[1]};
    }

    public int getFluxSize() {
        return fluxSize;
    }

    public int getNotificationCode() {
        return notificationCode;
    }

    public void setFluxSize(int fluxSize) {
        this.fluxSize = fluxSize;
    }

    public void setNotificationCode(int notificationCode) {
        this.notificationCode = notificationCode;
    }

}
