package com.iportbook.app.sponsor;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.TerminalScanner;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.net.DatagramSocketSender;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class AppSponsor extends ApplicationListener {
    private static final int SIZE_PROM = 300;
    private static final int SIZE_PUBL = 200;

    static {
        Logger.getGlobal().setUseParentHandlers(false);
    }

    private final String hostTCP;
    private final String ipDiff;
    private final int portTCP;
    private final int portDiff;

    private TerminalScanner termScanner = new TerminalScanner();
    private DataSocket daSo;
    private DatagramSocketSender udpSocket;

    private AppSponsor(String hostTCP, int portTCP, int portDiff, String ipDiff) throws IOException {
        this.hostTCP = hostTCP;
        this.portTCP = portTCP;
        this.portDiff = portDiff;
        this.ipDiff = ipDiff;
    }

    @Override
    protected void onStart() throws Exception {
        System.out.println("Connexion...");
        daSo = new DataSocket(hostTCP, portTCP);
        udpSocket = new DatagramSocketSender(portDiff, ipDiff);
        info();
        help();
        System.out.println();
    }

    @Override
    protected void onLoop() throws Exception {
        try {
            String[] input = termScanner.askNextLine("> ").split(" ", 2);
            switch (input[0]) {
                case "+prom": {
                    prom(input[1]);
                    break;
                }
                case "+publ": {
                    publ(udpSocket.getHost(), udpSocket.getPort(), input[1]);
                    break;
                }
                case "+help":
                    help();
                    break;
                case "+quit":
                    stop();
            }
        } catch (Exception ignored) {
            System.err.println("Erreur lors du traitement de votre requete.");
        }
    }

    @Override
    protected void onEnd() throws Exception {
        System.out.println("Déconnexion...");
        daSo.close();
        udpSocket.close();
    }

    private void info() {
        System.out.println("Informations:" +
                "\n\tport Diff:\t" + portDiff +
                "\n\tport TCP:\t" + portTCP +
                "\n\tip Diff:\t" + ipDiff +
                "\n\thost TCP:\t" + hostTCP);
    }

    private void help() {
        System.out.println("Utilisation:\n" +
                "\t+publ <msg>\tEnvoyer une publicité sur le serveur\n" +
                "\t+prom <msg>\tFaire une Promotion\n" +
                "\t+quit\t\tSe déconnecter\n" +
                "\t+help\t\tAide");
    }

    private void prom(String mess) throws Exception {
        if (mess.length() > SIZE_PROM)
            throw new Exception();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PROM ").append(mess);
        for (int i = 0; i < SIZE_PROM - mess.length(); i++)
            stringBuilder.append('#');
        udpSocket.send(stringBuilder.toString());
        System.out.println("La promotion à bien été transmise");
    }

    private void publ(String host, int port, String mess) throws Exception {
        if (mess.length() > SIZE_PUBL)
            throw new Exception();
        daSo.send(new MessageProcessor("PUBL?")
                .setIpDiff(host)
                .setPort(port)
                .setMess(mess)
                .build());
        MessageProcessor answer = daSo.readMessageProcessor();
        String type = answer.getType();
        if (Objects.equals(type, "PUBL>"))
            System.out.println("La publication à bien été transmise");
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: need 3 arguments <portServer> <ipDiff> <portDiff> (<hostServer>)");
            return;
        }
        final String hostServer = args.length > 3 ? args[3] : "localhost";
        final String ipDiff = args[1];
        final int portServer = Integer.parseInt(args[0]);
        final int portDiff = Integer.parseInt(args[2]);

        AppSponsor appSponsor = new AppSponsor(hostServer, portServer, portDiff, ipDiff);
        new Thread(appSponsor).start();
    }
}
