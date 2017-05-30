package com.iportbook.core.tools.message;

import com.iportbook.core.tools.Tools;

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

    public byte[] compose() {
        byte[] size = Tools.intToByteArray(fluxSize);
        return new byte[]{(byte) notificationCode, size[3], size[2]};
    }

    public static MessageUDP parse(byte[] bytes) {
        return new MessageUDP(bytes[0], Tools.byteArrayToInt(new byte[]{
                0,
                0,
                bytes[2],
                bytes[1]
        }));
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
