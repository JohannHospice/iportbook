package com.iportbook.app.client;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.message.MessageTCP;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;
import java.util.Scanner;

public class AppClient extends ApplicationListener {
    private final int portTCP;
    private final int portUDP;
    private final String host;
    private final Scanner scanner;
    private SocketHandler soHandler;

    private AppClient(String host, int portTCP, int portUDP) throws IOException {
        this.host = host;
        this.portTCP = portTCP;
        this.portUDP = portUDP;
        scanner = new Scanner(System.in);
    }

    @Override
    protected void onStart() throws Exception {
        // run udp notification handler
        new Thread(new NotificationHandler(portUDP)).start();
        // run tcp
        soHandler = new SocketHandler(host, portTCP);

        //conne regi
        System.out.println("connection: 0, register: 1");
        switch (getNext("\\d").charAt(0)) {
            case '0':
                //get id and pwd
                break;
            case '1':
                //get id, pwd and portUdp
                break;
        }
    }

    @Override
    protected void onLoop() throws Exception {
        // first send
        String text = scanner.nextLine();
        soHandler.sendMessage(textToMessageTCP(text));

        // then receive answer
        MessageTCP received = soHandler.receiveMessage();
        switch (received.getType()) {
            case WELCO:
                break;
            case GOBYE:
                break;
            case HELLO:
                break;
            case SSEM:
                break;
            case MUNEM:
                break;
            case OOLF:
                break;
            case EIRF:
                break;
            case OKIRF:
                break;
            case NOKRF:
                break;
            case ACKRF:
                break;
            case FRIEN:
                break;
            case NOFRI:
                break;
            case LBUP:
                break;
            case NOCON:
                break;
            case PUBL:
                break;
            case IQUIT:
                break;
        }
    }

    @Override
    protected void onEnd() throws Exception {
        soHandler.close();
        scanner.close();
    }

    /**
     * get MessageTCP object by raw text
     * transformation has to be determine there
     * @param text String
     * @return MessageTCP
     */
    private MessageTCP textToMessageTCP(String text) {
        // process an easiest protocol for text treatment
        return null;
    }

    /**
     * get next text wrote on terminal with a specific pattern(regex)
     * @param pattern String
     * @return String
     */
    private String getNext(String pattern) {
        String next = null;
        while (scanner.hasNext(pattern))
            next = scanner.next();
        return next;
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer portTCP");
            return;
        }
        AppClient appClient = new AppClient("localhost", Integer.parseInt(args[0]), 8383);
        new Thread(appClient).start();
    }
}
