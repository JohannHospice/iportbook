package com.iportbook.server;

import com.iportbook.app.MyRunnable;
import com.iportbook.app.modele.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Server extends MyRunnable {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> cliHandlers = new ArrayList<>();
    private ArrayList<Client> clients = new ArrayList<>();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    protected void onLoop() {
        LOGGER.info("waiting for client");
        try {
            ClientHandler client = new ClientHandler(this, serverSocket.accept());
            addClientHandler(client);
            new Thread(client).start();
            LOGGER.info("client added and handler started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() {
        for (ClientHandler client : cliHandlers) {
            client.stop();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("shutdown");
    }

    @Override
    protected void onStart() {
        LOGGER.info("start");
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        cliHandlers.remove(clientHandler);
    }

    public void addClientHandler(ClientHandler clientHandler) {
        cliHandlers.add(clientHandler);
    }

    public Client getClient(String id, String password) {
        Client client = null;
        Iterator<Client> iter = clients.iterator();

        while (client == null || iter.hasNext()) {
            Client next = iter.next();
            if (Objects.equals(next.getId(), id) && Objects.equals(next.getPassword(), password))
                client = next;
        }
        return client;
    }

    public void addClient(Client client) {

        clients.add(client);

    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer port");
            return;
        }

        Server server = new Server(Integer.parseInt(args[0]));
        new Thread(server).start();
    }
}
