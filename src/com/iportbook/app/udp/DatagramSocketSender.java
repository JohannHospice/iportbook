package com.iportbook.app.udp;

import java.io.IOException;
import java.net.*;

public class DatagramSocketSender {
    private final int targetPort;
    private final InetAddress address;
    private DatagramSocket dso;

    /**
     *
     * @param targetPort int
     * @param host string
     * @throws UnknownHostException
     * @throws SocketException
     */
    public DatagramSocketSender(int targetPort, String host) throws UnknownHostException, SocketException {
        this.targetPort = targetPort;
        this.address = InetAddress.getByName(host);
        this.dso = new DatagramSocket();
    }

    /**
     * with no host given it use localhost
     * @param targetPort int
     * @throws UnknownHostException
     * @throws SocketException
     */
    public DatagramSocketSender(int targetPort) throws UnknownHostException, SocketException {
        this.targetPort = targetPort;
        this.address = InetAddress.getByName("localhost");
        this.dso = new DatagramSocket();
    }

    public void send(String text) throws IOException {
        byte[] data = text.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, targetPort);
        dso.send(packet);
    }
}
