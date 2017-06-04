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
        new Thread(serverSponsor).start();
        serverClient.run();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: need 2 arguments");
            return;
        }
        final int portCli = Integer.parseInt(args[0]);
        final int portSpo = Integer.parseInt(args[1]);

        AppServer appServer = new AppServer(portCli, portSpo);
        appServer.start();
    }
}
