package com.iportbook.app.server.client;

import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.ServerSocket;


public class ServerClient extends ApplicationListener {
    private ServerSocket serverSocket;
    private ClientManager cliManager = new ClientManager("clients.bin");
    private int port;

    public ServerClient(int port) {
        this.port = port;
    }

    @Override
    public void onStart() throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.info("SERVER CLIENT: start / " + serverSocket.toString());
        try {
            cliManager.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoop() {
        LOGGER.info("waiting for client");
        try {
            cliManager.addClientHandler(serverSocket.accept());
            LOGGER.info("new client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnd() {
        cliManager.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("shutdown");
    }

    public ClientManager getClientManager() {
        return cliManager;
    }

    public int getPort() {
        return port;
    }
}
