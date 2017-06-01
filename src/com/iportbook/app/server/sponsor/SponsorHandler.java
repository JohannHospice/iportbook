package com.iportbook.app.server.sponsor;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;
import java.net.Socket;

public class SponsorHandler extends ApplicationListener {
    private final SocketHandler soHandler;

    SponsorHandler(Socket socket) throws IOException {
        this.soHandler = new SocketHandler(socket);
    }

    @Override
    protected void onStart() throws Exception {
        LOGGER.info(soHandler.receive());
    }

    @Override
    protected void onLoop() throws Exception {

    }

    @Override
    protected void onEnd() throws Exception {

    }
}
