package com.iportbook.app.client;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.message.MessageUDP;
import com.iportbook.core.tools.net.DatagramSocketReceiver;

import java.io.IOException;

public class NotificationHandler extends ApplicationListener {
    private final int portUdp;
    private final DatagramSocketReceiver datagramSocketReceiver;

    public NotificationHandler(int port) throws IOException {
        datagramSocketReceiver = new DatagramSocketReceiver();
        this.portUdp = port;
    }

    @Override
    protected void onStart() throws IOException {
        datagramSocketReceiver.bind(portUdp);
    }

    @Override
    protected void onLoop() throws IOException {
        MessageUDP messageUDP = datagramSocketReceiver.receiveMessageUDP();
        System.out.println("nb notif: " + messageUDP.getFluxSize());
        // different print switch notif
        switch (messageUDP.getNotificationCode()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    protected void onEnd() {
        datagramSocketReceiver.close();
    }
}
