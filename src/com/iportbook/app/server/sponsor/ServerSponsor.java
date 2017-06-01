package com.iportbook.app.server.sponsor;

import com.iportbook.core.tools.ApplicationListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerSponsor extends ApplicationListener {
    private ArrayList<SponsorHandler> sponsorHandlers = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port;

    public ServerSponsor(int port) {
        this.port = port;
    }

    @Override
    protected void onStart() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    protected void onLoop() throws IOException {
        try {
            SponsorHandler sponsorHandler = new SponsorHandler(serverSocket.accept());
            sponsorHandlers.add(sponsorHandler);
            new Thread(sponsorHandler).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() throws IOException {
        for (SponsorHandler spo : sponsorHandlers)
            spo.stop();
        serverSocket.close();
    }
}
