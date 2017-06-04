package com.iportbook.core.tools.processor;

/**
 * UDP message
 */
public class NotificationProcessor extends ByteProcessor {

    private static final int SIZE = 3;
    private static final int LENGTH_FLUX_SIZE = 2;
    private static final int POSITION_FLUX_SIZE = 1;
    private static final int POSITION_CODE = 0;

    public NotificationProcessor(byte code) {
        super(3);
        this.setCode(code);
    }

    public NotificationProcessor(byte code, int fluxSize) {
        this(code);
        setFluxSize(fluxSize);
    }

    public NotificationProcessor(byte[] data) {
        super(SIZE);
        setData(data, 0, SIZE);
    }

    public NotificationProcessor(int code, int fluxSize) {
        this((byte) code, fluxSize);
    }

    public NotificationProcessor(int code) {
        this((byte) code);
    }

    public void setCode(byte notificationCode) {
        setOffset(POSITION_CODE).set(notificationCode);
    }

    public void setFluxSize(int fluxSize) {
        setOffset(POSITION_FLUX_SIZE).setIntByLittleEndian(fluxSize);
    }

    public int getCode() {
        return setOffset(POSITION_CODE).getInt();
    }

    public int getFluxSize() {
        return setOffset(POSITION_FLUX_SIZE).getIntByLittleEndian(LENGTH_FLUX_SIZE);
    }

}
