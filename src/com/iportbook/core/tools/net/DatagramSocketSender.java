package com.iportbook.core.tools.net;

import com.iportbook.core.tools.message.MessageUDP;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class DatagramSocketSender {
    private final int targetPort;
    private final InetAddress address;
    private DatagramSocket dso;
    private Logger LOGGER = Logger.getLogger("data");

    /**
     * @param targetPort int
     * @param host       string
     */
    public DatagramSocketSender(int targetPort, String host) throws UnknownHostException, SocketException {
        this.targetPort = targetPort;
        this.address = InetAddress.getByName(host);
        this.dso = new DatagramSocket();
    }

    /**
     * with no host given it use localhost
     *
     * @param targetPort int
     */
    public DatagramSocketSender(int targetPort) throws UnknownHostException, SocketException {
        this.targetPort = targetPort;
        this.address = InetAddress.getByName("localhost");
        this.dso = new DatagramSocket();
    }

    public void send(String text) throws IOException {
        byte[] data = text.getBytes();
        send(data);
        dso.send(new DatagramPacket(data, data.length, address, targetPort));
    }

    public void send(byte[] data) throws IOException {
        dso.send(new DatagramPacket(data, data.length, address, targetPort));
        LOGGER.info("[" + Arrays.toString(data) + "]");
    }

    public void sendMessage(MessageUDP message) throws IOException {
        send(message.getBytes());
    }
    public void close() {
        dso.close();
    }

}
