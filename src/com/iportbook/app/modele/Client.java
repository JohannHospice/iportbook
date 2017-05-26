package com.iportbook.app.modele;

import java.util.ArrayList;

public class Client {
    private String id;
    private String password;
    private int port;
    private ArrayList<String> friendsId = new ArrayList<>();

    public Client(String id, String password, int port) {
        this.id = id;
        this.password = password;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}
