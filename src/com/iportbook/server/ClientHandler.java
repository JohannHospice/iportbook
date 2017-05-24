package com.iportbook.server;

import com.iportbook.app.MyRunnable;
import com.iportbook.app.message.Message;
import com.iportbook.app.modele.Client;
import com.iportbook.app.tcp.SocketHandler;
import com.iportbook.app.udp.DatagramSocketSender;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends MyRunnable {
    private final Server server;
    private int port;
    private SocketHandler soHandler;
    private DatagramSocketSender notifier;
    private Client client;

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.soHandler = new SocketHandler(socket);
        port = socket.getPort();
        this.server = server;
    }

    @Override
    protected void onStart() {
        try {
            Message message = Message.parse(soHandler.receive());
            String id;
            String password;
            switch (message.getType()) {
                case HELLO:
                    id = message.getArgument(0);
                    password = message.getArgument(1);
                    client = server.getClient(id, password);
                    break;
                case REGIS:
                    id = message.getArgument(0);
                    String port = message.getArgument(1);
                    password = message.getArgument(2);
                    client = new Client(id, password, Integer.parseInt(port));
                    break;
                default:
                    stop();
                    break;
            }
            //login or register
            //notifier = new DatagramSocketSender();
        } catch (Exception e) {
            stop();
            e.printStackTrace();
        }
    }

    @Override
    protected void onLoop() {
        try {
            soHandler.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() {
        try {
            soHandler.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        server.removeClientHandler(this);
    }

}