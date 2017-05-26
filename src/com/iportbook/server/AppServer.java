package com.iportbook.server;

import java.io.IOException;

public class AppServer {
    private ServerClient serverClient;
    private ServerSponsor serverSponsor;

    private AppServer(int port) throws IOException {
        serverClient = new ServerClient(port);
        serverSponsor = new ServerSponsor(8182);
    }

    private void start() {
        new Thread(serverClient).start();
        new Thread(serverSponsor).start();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer port");
            return;
        }
        AppServer appServer = new AppServer(Integer.parseInt(args[0]));
        appServer.start();
    }
}
