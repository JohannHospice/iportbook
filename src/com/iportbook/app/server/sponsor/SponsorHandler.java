package com.iportbook.app.server.sponsor;

import com.iportbook.app.server.client.ServerClient;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;
import java.net.Socket;

public class SponsorHandler implements Runnable {

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
