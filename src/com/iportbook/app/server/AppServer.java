package com.iportbook.app.server;

import com.iportbook.app.server.client.ServerClient;
import com.iportbook.app.server.sponsor.ServerSponsor;

import java.io.IOException;
import java.util.logging.Logger;

public class AppServer {
    private ServerClient serverClient;
    private ServerSponsor serverSponsor;

    private AppServer(int cliPort, int spoPort) throws IOException {
        serverClient = new ServerClient(cliPort);
        serverSponsor = new ServerSponsor(spoPort);
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java ChatServer port");
            return;
        }
        AppServer appServer = new AppServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        appServer.start();
    }

    private void start() {
        Logger.getGlobal().info("SERVER: start");
        new Thread(serverClient).start();
        //new Thread(serverSponsor).start();
    }
}
