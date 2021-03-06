package com.iportbook.core.modele;

import com.iportbook.core.tools.net.DatagramSocketSender;
import com.iportbook.core.tools.processor.NotificationProcessor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Client implements Serializable {
    private String id;
    private int password;
    private int portUDP;
    private ArrayList<Client> friendsId = new ArrayList<>();
    private ArrayList<Flux> flux = new ArrayList<>();

    public Client(String id, int password, int portUDP) {
        this.id = id;
        this.password = password;
        this.portUDP = portUDP;
    }

    public void addFriendsId(Client friend) {
        friendsId.add(friend);
    }

    public void addFlux(Flux flux) throws IOException {
        this.flux.add(flux);
    }

    public void addFluxNotify(Flux flux) throws IOException {
        addFlux(flux);
        DatagramSocketSender notifier = new DatagramSocketSender(getPortUDP());
        notifier.sendMessage(new NotificationProcessor(flux.getType(), getFluxSize()));
        notifier.close();
    }

    public Flux popFlux() {
        int size = flux.size();
        if (size <= 0)
            return null;
        Flux flux = this.flux.get(size - 1);
        removeFlux(flux);
        return flux;
    }

    public void removeFlux(Flux flux) {
        this.flux.remove(flux);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Client> getFriends() {
        return friendsId;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getPortUDP() {
        return portUDP;
    }

    public void setPortUDP(int portUDP) {
        this.portUDP = portUDP;
    }

    public Flux getFlux(int i) {
        return flux.get(i);
    }

    public int getFluxSize() {
        return flux.size();
    }

    public void addFlux(int type, byte[] message) throws IOException {
        addFlux(new Flux(type, message));
    }

    public boolean hasFriend(String id) {
        boolean hasClient = false;
        Iterator<Client> iter = friendsId.iterator();
        while (!hasClient && iter.hasNext()) {
            Client next = iter.next();
            if (Objects.equals(next.getId(), id))
                hasClient = true;
        }
        return hasClient;
    }
}
