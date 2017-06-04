package com.iportbook.app.server.sponsor;

import com.iportbook.app.server.ServerListener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSponsor extends ServerListener {
    private ArrayList<SponsorHandler> sponsorHandlers = new ArrayList<>();

    public ServerSponsor(int port) {
        super(port);
    }

    @Override
    protected void start() {

    }

    @Override
    protected void onAccept(Socket accept) throws IOException {
        SponsorHandler sponsorHandler = new SponsorHandler(accept);
        sponsorHandlers.add(sponsorHandler);
        new Thread(sponsorHandler).start();
    }

    @Override
    protected void close() {
        for (SponsorHandler spo : sponsorHandlers)
            spo.stop();
    }
}
