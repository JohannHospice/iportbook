package com.iportbook.app.sponsor;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.TerminalScanner;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.net.DatagramSocketSender;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.util.Objects;

public class AppSponsor extends ApplicationListener {
    private static final int SIZE_PROM = 300;
    private static final int SIZE_PUBL = 200;
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

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: need 3 arguments");
            return;
        }
        final String hostServer = "localhost";
        final String ipDiff = args[1];
        final int portServer = Integer.parseInt(args[0]);
        final int portDiff = Integer.parseInt(args[2]);

        AppSponsor appSponsor = new AppSponsor(hostServer, portServer, portDiff, ipDiff);
        new Thread(appSponsor).start();
    }

    @Override
    protected void onStart() throws Exception {
        daSo = new DataSocket(hostTCP, portTCP);
        udpSocket = new DatagramSocketSender(portDiff, ipDiff);
    }

    @Override
    protected void onLoop() throws Exception {
        switch (termScanner.ask("promotion(0), publicite(1), quit(2)", "^(0|1|2)$").charAt(0)) {
            case '0': {
                String mess = termScanner.ask("message: ", ".{0," + SIZE_PROM + "}");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PROM ").append(mess);
                for (int i = 0; i < SIZE_PROM - mess.length(); i++)
                    stringBuilder.append('#');
                udpSocket.send(stringBuilder.toString());
                break;
            }
            case '1': {
                String mess = termScanner.ask("message: ", ".{0," + SIZE_PUBL + "}");
                daSo.send(new MessageProcessor("PUBL?")
                        .setIpDiff(udpSocket.getHost())
                        .setPort(udpSocket.getPort())
                        .setMess(mess)
                        .build());
                MessageProcessor answer = new MessageProcessor(daSo.read());
                String type = answer.getType();
                if (Objects.equals(type, "PUBL>"))
                    System.out.println("La publication à bien été retransmise");
                break;
            }
            case '2':
                stop();
        }
    }

    @Override
    protected void onEnd() throws Exception {
        daSo.close();
        udpSocket.close();
    }
}
