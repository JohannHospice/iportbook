package com.iportbook.app.client;

import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DatagramSocketReceiver;
import com.iportbook.core.tools.processor.NotificationProcessor;

import java.io.IOException;

public class NotificationHandler extends ApplicationListener {
    private final DatagramSocketReceiver datagramSocketReceiver;
    private int port;

    public NotificationHandler(int port) throws IOException {
        datagramSocketReceiver = new DatagramSocketReceiver();
        this.port = port;
    }

    @Override
    protected void onStart() throws IOException {
        datagramSocketReceiver.bind(port);
        port = datagramSocketReceiver.getLocalPort();
    }

    @Override
    protected void onLoop() throws IOException {
        NotificationProcessor notificationProcessor = datagramSocketReceiver.receiveMessageUDP();
        System.out.println("nb notif: " + notificationProcessor.getFluxSize());
        //TODO: different print switch notif
        switch (notificationProcessor.getCode()) {
            case Flux.FLOO:
                break;
            case Flux.FRIE:
                break;
            case Flux.MESS:
                break;
            case Flux.NOKRF:
                break;
            case Flux.OKIRF:
                break;
            case Flux.PUBL:
                break;
        }
    }

    @Override
    protected void onEnd() {
        datagramSocketReceiver.close();
    }

    public int getPort() {
        return port;
    }
}
