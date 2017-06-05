package com.iportbook.app.server;

import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public abstract class ServerListener extends ApplicationListener {
    private ServerSocket serverSocket;
    private int port;

    public ServerListener(int port) {
        this.port = port;
    }

    @Override
    protected void onStart() throws IOException {
        serverSocket = new ServerSocket(port);
        Logger.getGlobal().info("listen: " + serverSocket.toString());
    }

    @Override
    protected void onLoop() {
        try {
            Socket so = serverSocket.accept();
            Logger.getGlobal().info("accept: " + so);
            onAccept(so);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.getGlobal().info("shutdown");
    }

    protected abstract void onAccept(Socket accept) throws IOException;

    public int getPort() {
        return port;
    }

}
