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
        datagramSocketReceiver = new DatagramSocketReceiver(port);
        this.port = port;
    }

    @Override
    protected void onStart() throws IOException {
        port = datagramSocketReceiver.getLocalPort();
    }

    @Override
    protected void onLoop() throws IOException {
        NotificationProcessor notificationProcessor = datagramSocketReceiver.receiveMessageUDP();
        System.out.println("nb notif: " + notificationProcessor.getFluxSize());
        //TODO: different print switch notif
        switch (notificationProcessor.getCode()) {
            case Flux.FLOO:
            	System.out.println("Recu un flood");
                break;
            case Flux.FRIE:
            	System.out.println("Recu une requete d'ami");
                break;
            case Flux.MESS:
            	System.out.println("Recu un message prive");
                break;
            case Flux.NOKRF:
            	System.out.println("Requete d'ami refusee");
                break;
            case Flux.OKIRF:
            	System.out.println("Requete d'ami acceptee");
                break;
            case Flux.PUBL:
            	System.out.println("Recu une publicite");
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
