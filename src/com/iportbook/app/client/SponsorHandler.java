package com.iportbook.app.client;

import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class SponsorHandler extends ApplicationListener {
    private final String host;
    private final int port;
    private MulticastSocket multicastSocket;
    private DatagramPacket packet;

    SponsorHandler(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void onStart() throws Exception {
        this.multicastSocket = new MulticastSocket(port);
        this.multicastSocket.joinGroup(InetAddress.getByName(host));
        byte[] data = new byte[305];
        packet = new DatagramPacket(data, data.length);
    }

    @Override
    protected void onLoop() throws Exception {
        multicastSocket.receive(packet);
        String str = new String(packet.getData(), 0, packet.getLength());
        System.out.println("\n(" + host + ":" + port + ")pro> " + getMessage(str) + "\n> ");
    }

    private String getMessage(String str) {
        int i = str.length() - 1;
        while (i >= 0 && str.charAt(i) == '#')
            i--;
        return str.substring(5, i);
    }

    @Override
    protected void onEnd() throws Exception {
        multicastSocket.close();
    }
}