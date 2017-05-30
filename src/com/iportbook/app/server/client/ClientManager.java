package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.io.Serializer;
import com.iportbook.core.tools.message.MessageUDP;
import com.iportbook.core.tools.net.DatagramSocketSender;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ClientManager {
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<ClientHandler> cliHandlers = new ArrayList<>();
    private String filename;

    public ClientManager(String filename) {
        this.filename = filename;
    }

    public void addFluxToClient(String id, Flux flux) {
        try {
            Client client = getClient(id);
            client.addFlux(flux);
            notifyClient(client, flux);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public void notifyClient(Client client, Flux flux) {
        try {
            DatagramSocketSender notifier = new DatagramSocketSender(client.getPortUDP());
            notifier.send(new MessageUDP(flux.getType(), client.getFluxSize()).compose());
            notifier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void store() throws IOException {
        Serializer.write(filename, clients);
    }

    public void restore() throws IOException, ClassNotFoundException {
        clients.addAll((Collection<? extends Client>) Serializer.read(filename));
    }

    public void stop() {
        for (ClientHandler client : cliHandlers)
            client.stop();
    }

    public void addClient(Client client) throws ClientException {
        if (hasClientById(client.getId()))
            throw new ClientException("client already exist");
        clients.add(client);
    }

    public void addClient(String id, int password, int udpPort) throws ClientException {
        addClient(new Client(id, password, udpPort));
    }

    public void addClientHandler(Socket accept) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, accept);
        cliHandlers.add(clientHandler);
        new Thread(clientHandler).start();
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        cliHandlers.remove(clientHandler);
    }

    public boolean hasClientById(String id) {
        boolean hasClient = false;
        Iterator<Client> iter = clients.iterator();
        while (!hasClient && iter.hasNext()) {
            Client next = iter.next();
            if (Objects.equals(next.getId(), id))
                hasClient = true;
        }
        return hasClient;
    }

    public Client getClient(String id, String password) throws ClientException {
        Client client = null;
        Iterator<Client> iter = clients.iterator();
        while (client == null || iter.hasNext()) {
            Client next = iter.next();
            if (Objects.equals(next.getId(), id) && Objects.equals(next.getPassword(), password))
                client = next;
        }
        if (client == null)
            throw new ClientException("no client founded");
        return client;
    }

    public Client getClient(String id) throws ClientException {
        Client client = null;
        Iterator<Client> iter = clients.iterator();
        while (client == null || iter.hasNext()) {
            Client next = iter.next();
            if (Objects.equals(next.getId(), id))
                client = next;
        }
        if (client == null)
            throw new ClientException("no client founded");
        return client;
    }

    public Client getClient(int i) {
        return clients.get(i);
    }

    public int getClientSize() {
        return clients.size();
    }
}
