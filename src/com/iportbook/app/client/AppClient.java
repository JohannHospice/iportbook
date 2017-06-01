package com.iportbook.app.client;

import java.io.IOException;

public class AppClient {

    private ProcessorClient processorClient;

    private AppClient(int port) throws IOException {
        processorClient = new ProcessorClient("localhost", port);
    }

    private void start() {
        new Thread(processorClient).start();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer port");
            return;
        }
        AppClient appClient = new AppClient(Integer.parseInt(args[0]));
        appClient.start();
    }
}
