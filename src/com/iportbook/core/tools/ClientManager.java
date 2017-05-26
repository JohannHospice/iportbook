package com.iportbook.core.tools;

import com.iportbook.app.server.client.ClientException;
import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.io.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class ClientManager {
    private ArrayList<Client> clients = new ArrayList<>();
    private String filename;

    public ClientManager(String filename) {
        this.filename = filename;
    }

    public void addClient(Client client) throws ClientException {
        if (!hasClientById(client.getId())) {
            clients.add(client);
        } else throw new ClientException("client already exist");
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

    public Client get(int i) {
        return clients.get(i);
    }

    public void store() throws IOException {
        Serializer.write(filename, clients);
    }

    public void restore() throws IOException, ClassNotFoundException {
        clients.addAll((Collection<? extends Client>) Serializer.read(filename));
    }

    public int size() {
        return clients.size();
    }

    public void forEach(Consumer<Client> action) {
        clients.forEach(action);
    }
}
