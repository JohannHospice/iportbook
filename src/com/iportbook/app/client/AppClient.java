package com.iportbook.app.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;

public class AppClient extends ApplicationListener {
    private final int port;
    private Client client;

    public AppClient(int port) {
        this.port = port;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onLoop() {

    }

    @Override
    protected void onEnd() {

    }
}
