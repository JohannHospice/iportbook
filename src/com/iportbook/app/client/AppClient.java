package com.iportbook.app.client;

import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.util.Scanner;

public class AppClient extends ApplicationListener {
    private final Scanner scanner;
    private final String host;
    private int portTCP;
    /**
     * allow you to send and receive TCP request
     */
    private DataSocket daSo;
    private NotificationHandler notificationHandler;

    private AppClient(String host, int portTCP, int portUDP) throws IOException {
        this.host = host;
        this.portTCP = portTCP;
        scanner = new Scanner(System.in);
        notificationHandler = new NotificationHandler(portUDP);
        daSo = new DataSocket();
    }

    @Override
    protected void onStart() throws Exception {
        // run udp notification handler
        new Thread(notificationHandler).start();
        // run tcp
        daSo.bind(host, portTCP);
        portTCP = daSo.getLocalPort();

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
        daSo.send(inputToMessageProcessor(text).build());

        // then receive answer
        MessageProcessor received = new MessageProcessor(daSo.read());
        switch (received.getType()) {
        }
    }

    @Override
    protected void onEnd() throws Exception {
        daSo.close();
        scanner.close();
    }

    /**
     * get MessageTCP object by raw text
     * transformation has to be determine there
     *
     * @param text String
     * @return MessageTCP
     */
    private MessageProcessor inputToMessageProcessor(String input) {
        // process an easiest protocol for text treatment
        return null;
    }

    /**
     * get next text wrote on terminal with a specific pattern(regex)
     *
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
