package com.iportbook.app.server;

import com.iportbook.app.server.client.ServerClient;
import com.iportbook.app.server.sponsor.ServerSponsor;

import java.io.IOException;

public class AppServer {
    private ServerClient serverClient;
    private ServerSponsor serverSponsor;

    private AppServer(int cliPort, int spoPort) throws IOException {
        serverClient = new ServerClient(cliPort);
        serverSponsor = new ServerSponsor(spoPort);
    }

    private void start() {
        new Thread(serverClient).start();
        //    new Thread(serverSponsor).start();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer port");
            return;
        }
        AppServer appServer = new AppServer(Integer.parseInt(args[0]), 9999);
        appServer.start();
    }
}
