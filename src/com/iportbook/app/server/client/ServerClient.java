package com.iportbook.app.server.client;

import com.iportbook.app.server.ServerListener;

import java.io.IOException;
import java.net.Socket;


public class ServerClient extends ServerListener {
    private ClientManager cliManager = ClientManager.getInstance();

    public ServerClient(int port) {
        super(port);
    }

    @Override
    protected void start() {
        try {
            cliManager.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void close() {
        cliManager.stop();
        LOGGER.info("shutdown");
    }

    @Override
    protected void onAccept(Socket socket) throws IOException {
        cliManager.addClientHandler(socket);
    }

    public ClientManager getClientManager() {
        return cliManager;
    }

}
