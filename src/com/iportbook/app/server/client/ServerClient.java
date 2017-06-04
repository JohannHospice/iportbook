package com.iportbook.app.server.client;

import com.iportbook.app.server.ServerListener;
import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerClient extends ServerListener {
    private ServerSocket serverSocket;
    private ClientManager cliManager = new ClientManager("clients.bin");

    public ServerClient(int port) {
        super(port);
    }

    @Override
    public void onStart() throws IOException {
        super.onStart();
        try {
            cliManager.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();
        cliManager.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("shutdown");
    }

    @Override
    protected void onAccept(Socket accept) {
        try {
            cliManager.addClientHandler(accept);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClientManager getClientManager() {
        return cliManager;
    }

}
