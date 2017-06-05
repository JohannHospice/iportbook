package com.iportbook.app.client;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.TerminalScanner;
import com.iportbook.core.tools.net.DataSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class AppClientAbstract extends ApplicationListener {
    static {
        Logger.getGlobal().setUseParentHandlers(false);
    }

    ArrayList<SponsorHandler> sponsorHandlers = new ArrayList<>();
    private final String host;
    private int portTCP;
    protected String id;
    private NotificationHandler notificationHandler;
    TerminalScanner termScanner = new TerminalScanner();
    /**
     * allow you to send and receive TCP request
     */
    DataSocket daSo;

    AppClientAbstract(String host, int portTCP, int portUDP) throws IOException {
        this.host = host;
        this.portTCP = portTCP;
        notificationHandler = new NotificationHandler(portUDP);
    }

    @Override
    protected void onStart() throws Exception {
        // run udp notification handler
        new Thread(notificationHandler).start();
        // run tcp
        daSo = new DataSocket(host, portTCP);
        portTCP = daSo.getLocalPort();

        //conne regis
        char type = termScanner.askNext("connection(0), register(1): ", "\\d").charAt(0);
        id = termScanner.askNext("identifiant: ", "\\w{1,8}");
        int password = Integer.parseInt(termScanner.askNext("password: ", "\\d{1,5}"));

        switch (type) {
            case '0':
                conne(id, password);
                break;
            case '1':
                int port = notificationHandler.getPort();
                regis(id, password, port);
                break;
        }
    }

    @Override
    protected void onLoop() throws Exception {
        try {

            String input = termScanner.askNextLine(id + "> ", 300);

            if (input.charAt(0) == '+') {
                String command[] = input.split(" ", 2);
                switch (command[0]) {
                    case "+quit":
                        iquit();
                        break;
                    case "+consu":
                        consu();
                        break;
                    case "+list":
                        list();
                        break;
                    case "+frie": {
                        String id = command[1];
                        frie(id);
                        break;
                    }
                    case "+abo": {
                        String[] address = command[1].split(":");
                        String host = address[0];
                        int port = Integer.parseInt(address[1]);
                        abo(host, port);
                        break;
                    }
                    default: {
                        String id = command[0].substring(1, command[0].length());
                        mess(id, command[1]);
                        break;
                    }
                }
            } else if (!input.isEmpty()) {
                floo(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() throws Exception {
        for (SponsorHandler spo : sponsorHandlers)
            spo.stop();
        daSo.close();
        notificationHandler.stop();
        termScanner.close();
    }

    abstract void abo(String host, int port) throws IOException;

    abstract void regis(String id, int password, int port) throws Exception;

    abstract void conne(String id, int password) throws Exception;

    abstract void mess(String id, String mess) throws Exception;

    abstract void frie(String id) throws Exception;

    abstract void floo(String mess) throws Exception;

    abstract void consu() throws Exception;

    abstract void list() throws Exception;

    abstract void iquit() throws Exception;

}
