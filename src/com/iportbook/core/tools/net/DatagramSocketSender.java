package com.iportbook.core.tools.net;

import java.io.IOException;
import java.net.*;

public class DatagramSocketSender {
    private final int targetPort;
    private final InetAddress address;
    private DatagramSocket dso;

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
        dso.send(new DatagramPacket(data, data.length, address, targetPort));
    }

    public void close() {
        dso.close();
    }

}
