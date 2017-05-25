package com.iportbook.app.net.udp;

import java.io.IOException;
import java.net.*;

public class DatagramSocketHandler {
    private DatagramSocketReceiver receiver;
    private DatagramSocketSender sender;

    public DatagramSocketHandler(int sourcePort, int targetPort, String host) throws UnknownHostException, SocketException {
        sender = new DatagramSocketSender(targetPort, host);
        receiver = new DatagramSocketReceiver(sourcePort);
    }

    public DatagramSocketHandler(int sourcePort, int targetPort) throws UnknownHostException, SocketException {
        sender = new DatagramSocketSender(targetPort);
        receiver = new DatagramSocketReceiver(sourcePort);
    }

    public void send(String text) throws IOException {
        sender.send(text);
    }

    public String receive() throws IOException {
        return receiver.receive();
    }
}
