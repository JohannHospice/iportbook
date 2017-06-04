package com.iportbook.core.tools.net;

import com.iportbook.core.tools.processor.NotificationProcessor;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class DatagramSocketReceiver {
    private final static int LENGTH = 200;
    private DatagramSocket dso;

    public DatagramSocketReceiver() throws UnknownHostException, SocketException {
        this.dso = new DatagramSocket();
    }

    public DatagramSocketReceiver(int sourcePort) throws UnknownHostException, SocketException {
        this.dso = new DatagramSocket(sourcePort);
    }

    public void bind(int sourcePort) throws SocketException {
        dso.bind(new InetSocketAddress(sourcePort));
    }

    public int getLocalPort() {
        return dso.getLocalPort();
    }

    public String receive() throws IOException {
        byte[] data = new byte[LENGTH];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        dso.receive(packet);
        String str = new String(packet.getData(), 0, packet.getLength());
        Logger.getGlobal().info(Arrays.toString(packet.getData()));
        return str;
    }

    public NotificationProcessor receiveMessageUDP() throws IOException {
        byte[] data = new byte[3];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        dso.receive(packet);
        return new NotificationProcessor(packet.getData());
    }

    public void close() {
        dso.close();
    }
}
