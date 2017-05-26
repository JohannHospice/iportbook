package com.iportbook.app.server.client;

import com.iportbook.core.tools.ClientManager;
import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.net.DatagramSocketSender;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.composed.ComposedText;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class ServerClient extends ApplicationListener {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> cliHandlers = new ArrayList<>();
    ClientManager cliManager = new ClientManager("clients");
    private int port;

    public ServerClient(int port) {
        this.port = port;
    }

    @Override
    public void onStart() throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.info("start: " + serverSocket.toString());
    }

    @Override
    public void onLoop() {
        LOGGER.info("waiting for client");
        try {
            ClientHandler clientHandler = new ClientHandler(this, serverSocket.accept());
            cliHandlers.add(clientHandler);
            new Thread(clientHandler).start();
            LOGGER.info("client added and handler started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnd() {
        for (ClientHandler client : cliHandlers)
            client.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("shutdown");
    }

    void removeClientHandler(ClientHandler clientHandler) {
        cliHandlers.remove(clientHandler);
    }

    public boolean notify(String id, ComposedText event) {
        LOGGER.info("notify");
        try {
            Client client = cliManager.getClient(id);
            DatagramSocketSender notifier = new DatagramSocketSender(client.getPort());
            notifier.send(event.pack());
            notifier.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Client getClientById(String id) throws ClientException {
        return cliManager.getClient(id);
    }

    public void addClient(Client client) throws ClientException {
        cliManager.addClient(client);
    }

}
