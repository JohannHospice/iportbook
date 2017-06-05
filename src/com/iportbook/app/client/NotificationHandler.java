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
        int fluxSize = notificationProcessor.getFluxSize();
        int code = notificationProcessor.getCode();

        StringBuilder builder = new StringBuilder("(" + fluxSize + ") ");
        switch (code) {
            case Flux.FLOO:
                builder.append("Innondation");
                break;
            case Flux.FRIE:
                builder.append("Requete d'ami");
                break;
            case Flux.MESS:
                builder.append("Message prive");
                break;
            case Flux.NOKRF:
                builder.append("Requete d'ami refusee");
                break;
            case Flux.OKIRF:
                builder.append("Requete d'ami acceptee");
                break;
            case Flux.PUBL:
                builder.append("Publicite");
                break;
        }
        builder.append("\n> ");
        System.out.print(builder.toString());
    }

    @Override
    protected void onEnd() {
        datagramSocketReceiver.close();
    }

    public int getPort() {
        return port;
    }
}
