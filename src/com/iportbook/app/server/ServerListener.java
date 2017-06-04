package com.iportbook.app.server;

import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ServerListener extends ApplicationListener {
    private ServerSocket serverSocket;
    private int port;

    public ServerListener(int port) {
        this.port = port;
    }

    @Override
    public void onStart() throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.info("listen: " + serverSocket.toString());
    }

    @Override
    public void onLoop() {
        try {
            Socket so = serverSocket.accept();
            LOGGER.info("accept: " + so);
            onAccept(so);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnd() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("shutdown");
    }

    protected abstract void onAccept(Socket accept);

    public int getPort() {
        return port;
    }

}
