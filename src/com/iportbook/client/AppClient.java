package com.iportbook.client;

import com.iportbook.app.modele.Client;
import com.iportbook.app.tools.ApplicationListener;

public class AppClient extends ApplicationListener {
    private final int port;
    private Client client;
    public AppClient(int port){
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
