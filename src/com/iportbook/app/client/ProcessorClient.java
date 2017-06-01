package com.iportbook.app.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;

public class ProcessorClient extends ApplicationListener {
    private final int port;
    private final String host;
    private SocketHandler soHandler = null;

    public ProcessorClient(String host, int port) throws IOException {
        this.port = port;
        this.host = host;
    }

    @Override
    protected void onStart() throws IOException {
        soHandler = new SocketHandler(host, port);

    }

    @Override
    protected void onLoop() {

    }

    @Override
    protected void onEnd() {

    }
}
