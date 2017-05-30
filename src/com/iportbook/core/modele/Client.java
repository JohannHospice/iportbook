package com.iportbook.core.modele;

import java.util.ArrayList;

public class Client {
    private String id;
    private int password;
    private int portUDP;
    private ArrayList<String> friendsId = new ArrayList<>();
    private ArrayList<Flux> flux = new ArrayList<>();

    public Client(String id, int password, int portUDP) {
        this.id = id;
        this.password = password;
        this.portUDP = portUDP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getFriendsId() {
        return friendsId;
    }

    public void addFriendsId(String id) {
        friendsId.add(id);
    }

    public void setFriendsId(ArrayList<String> friendsId) {
        this.friendsId = friendsId;
    }

    public Flux getFlux(int i) {
        return flux.get(i);
    }

    public void addFlux(Flux message) {
        this.flux.add(message);
    }

    public int getFluxSize() {
        return flux.size();
    }
}
