package com.iportbook.server;

import com.iportbook.app.net.tcp.SocketHandler;

import java.io.IOException;
import java.net.Socket;

public class SponsorHandler implements Runnable{

    private final ServerClient serverClient;
    private final SocketHandler soHandler;

    protected SponsorHandler(ServerClient serverClient, Socket socket) throws IOException {
        this.serverClient = serverClient;
        this.soHandler = new SocketHandler(socket);
    }

    @Override
    public void run() {

    }
}
