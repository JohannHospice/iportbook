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

    private final String host;
    public int port;
    protected String id;
    ArrayList<SponsorHandler> sponsorHandlers = new ArrayList<>();
    TerminalScanner termScanner = new TerminalScanner();
    /**
     * allow you to send and receive TCP request
     */
    DataSocket daSo;
    private int portTCP;
    private NotificationHandler notificationHandler;

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
        char type = termScanner.askNext("Que voulez vous faire?\n0: Connexion\n1: Inscription\n> ", "^(0|1)$").charAt(0);

        switch (type) {
            case '0': {
                String[] input = termScanner.askNext("Saisissez vos identifiants [<id>:<pwd>]:\n> ", "\\w{1,8}:\\d{1,5}").split(":");
                conne(input[0], Integer.parseInt(input[1]));
                break;
            }
            case '1': {
                id = termScanner.askNext("identifiant: ", "\\w{1,8}");
                int password = Integer.parseInt(termScanner.askNext("password: ", "\\d{1,5}"));
                int port = notificationHandler.getPort();
                regis(id, password, port);
                break;
            }
        }
        help();
    }

    @Override
    protected void onLoop() throws Exception {
        try {
            String input = termScanner.askNextLine(id + "> ");
            switch (input.charAt(0)) {
                case '@': {
                    String command[] = input.split(" ", 2);
                    String id = command[0].substring(1, command[0].length());
                    mess(id, command[1]);
                    break;
                }
                case '+': {
                    String command[] = input.split(" ", 2);
                    String type = command[0];
                    switch (type) {
                        case "+add": {
                            String name = command[1];
                            switch (name.charAt(0)) {
                                case '#':
                                    String[] address = name.substring(1).split(":");
                                    if (address.length == 2)
                                        abo(address[0], Integer.parseInt(address[1]));
                                    break;
                                case '@':
                                    frie(name.substring(1));
                                    break;
                            }
                            break;
                        }
                        case "+help":
                            help();
                            break;
                        case "+quit":
                            iquit();
                            break;
                        case "+consu":
                            consu();
                            break;
                        case "+list":
                            list();
                            break;
                    }
                    break;
                }
                default: {
                    floo(input);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void help() {
        System.out.println("Utilisation:\n" +
                "\t<msg>\t\t\t\tInnoder\n" +
                "\t@<id> <msg>\t\t\tenvoyer un message privé\n" +
                "\t+add @<id>\t\t\tEnvoyer une demande d'ami\n" +
                "\t+add #<ip>:<port>\tS'abonner à un promotteur\n" +
                "\t+consu\t\t\t\tConsulation des notifications\n" +
                "\t+list\t\t\t\tAfficher la liste des clients\n" +
                "\t+quit\t\t\t\tSe déconnecter");
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
