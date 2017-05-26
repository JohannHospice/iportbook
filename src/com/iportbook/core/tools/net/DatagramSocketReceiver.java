package com.iportbook.core.tools.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramSocketReceiver {
    private DatagramSocket dso;
    private final static int LENGTH = 200;

    public DatagramSocketReceiver(int sourcePort) throws UnknownHostException, SocketException {
        this.dso = new DatagramSocket(sourcePort);
    }

    public String receive() throws IOException {
        byte[] data = new byte[LENGTH];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        dso.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }
}
