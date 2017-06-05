package com.iportbook.app.server.client;

import com.iportbook.app.server.ServerListener;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;


public class ServerClient extends ServerListener {
    private ClientManager cliManager = ClientManager.getInstance();

    public ServerClient(int port) {
        super(port);
    }

    @Override
    protected void onStart() throws IOException {
        super.onStart();
        try {
            cliManager.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() {
        cliManager.stop();
        Logger.getGlobal().info("shutdown");
        super.onEnd();
    }

    @Override
    protected void onAccept(Socket socket) throws IOException {
        cliManager.addClientHandler(socket);
    }

    public ClientManager getClientManager() {
        return cliManager;
    }

}
