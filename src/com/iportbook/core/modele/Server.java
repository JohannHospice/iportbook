package com.iportbook.core.modele;

public class Server {
    private int portClient;
    private int portSponsor;

    public Server(int portClient, int portSponsor) {
        this.portClient = portClient;
        this.portSponsor = portSponsor;
    }

    public int getPortClient() {
        return portClient;
    }

    public void setPortClient(int portClient) {
        this.portClient = portClient;
    }

    public int getPortSponsor() {
        return portSponsor;
    }

    public void setPortSponsor(int portSponsor) {
        this.portSponsor = portSponsor;
    }
}
