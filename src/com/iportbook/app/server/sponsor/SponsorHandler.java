package com.iportbook.app.server.sponsor;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;

import java.io.IOException;
import java.net.Socket;

public class SponsorHandler extends ApplicationListener {
    private final DataSocket daSo;

    SponsorHandler(Socket socket) throws IOException {
        this.daSo = new DataSocket(socket);
    }

    @Override
    protected void onStart() throws Exception {
        daSo.read();
    }

    @Override
    protected void onLoop() throws Exception {

    }

    @Override
    protected void onEnd() throws Exception {

    }
}
