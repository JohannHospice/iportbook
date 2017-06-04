package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.io.Serializer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ClientManager {
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<ClientHandlerAbstract> cliHandlers = new ArrayList<>();
    private String filename;

    public ClientManager(String filename) {
        this.filename = filename;
    }

    public void store() throws IOException {
        Serializer.write(filename, clients);
    }

    public void restore() throws IOException, ClassNotFoundException {
        clients.addAll((Collection<? extends Client>) Serializer.read(filename));
    }

    public void stop() {
        for (ClientHandlerAbstract client : cliHandlers)
            client.stop();
        try {
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFlux(String id, Flux flux) throws Exception {
        getClient(id).addFluxNotify(flux);
        try {
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void floodFriend(Client client, Flux flux) throws ClientException, IOException {
        floodFriend(client, flux, new ArrayList<>());
        try {
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void floodFriend(Client client, Flux flux, ArrayList<String> visited) throws IOException, ClientException {
        client.addFluxNotify(flux);
        visited.add(client.getId());
        for (Client friend : client.getFriends())
            if (!visited.contains(client.getId()))
                floodFriend(friend, flux);
    }

    public void addFriendship(String id1, String id2) throws ClientException {
        if (Objects.equals(id1, id2))
            throw new ClientException("same client");
        Client cli1 = getClient(id1);
        Client cli2 = getClient(id2);
        cli2.addFriendsId(cli1);
        cli1.addFriendsId(cli2);
        try {
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(Client client) throws ClientException {
        if (hasClientById(client.getId()))
            throw new ClientException("client already exist");
        clients.add(client);
        try {
            store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String id, int password, int udpPort) throws ClientException {
        addClient(new Client(id, password, udpPort));
    }

    public void addClientHandler(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, socket);
        cliHandlers.add(clientHandler);
        new Thread(clientHandler).start();
    }

    public void removeClientHandler(ClientHandlerAbstract clientHandler) {
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

    public Client getClient(String id, int password) throws ClientException {
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
