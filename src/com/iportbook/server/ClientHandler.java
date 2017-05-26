package com.iportbook.server;

import com.iportbook.app.net.tcp.SocketHandler;
import com.iportbook.app.tools.ApplicationListener;
import com.iportbook.app.tools.ComposedText;
import com.iportbook.app.tools.ComposedTextById;
import com.iportbook.app.tools.Message;
import com.iportbook.app.modele.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import static com.iportbook.app.tools.Message.Type.*;

public class ClientHandler extends ApplicationListener {
    private Client client;
    private final ServerClient serverClient;
    private final SocketHandler soHandler;
    private HashMap<Message.Type, ComposedText> composedTextHashMap = new HashMap<>();

    ClientHandler(ServerClient serverClient, Socket socket) throws IOException {
        this.serverClient = serverClient;
        this.soHandler = new SocketHandler(socket);
    }

    @Override
    protected void onStart() {
        try {
            Message message = soHandler.receiveMessage();
            switch (message.getType()) {
                case CONNE:
                    client = serverClient.cliManager.getClient(
                            message.getArgument(0),
                            message.getArgument(1));
                    soHandler.sendMessage(new Message(Message.Type.HELLO));
                    break;
                case REGIS:
                    client = new Client(
                            message.getArgument(0),
                            message.getArgument(2),
                            Integer.parseInt(message.getArgument(1)));
                    serverClient.cliManager.addClient(client);
                    soHandler.sendMessage(new Message(Message.Type.WELCO));
                    break;
                default:
                    stop();
                    break;
            }
        } catch (ClientException e) {
            try {
                soHandler.sendMessage(new Message(Message.Type.GOBYE));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    @Override
    protected void onLoop() {
        try {
            Message message = soHandler.receiveMessage();
            switch (message.getType()) {
                case FRIE:
                    if (message.getOperator() == Message.Operator.ASK) {
                        String id = message.getArgument(0);
                        if (serverClient.cliManager.hasClientById(id)) {
                            soHandler.sendMessage(new Message(Message.Type.FRIE, Message.Operator.CRIGHT));
                        } else {
                            soHandler.sendMessage(new Message(Message.Type.FRIE, Message.Operator.CLEFT));
                        }
                    }
                    break;
                case MENUM:
                    if (composedTextHashMap.containsKey(MESS)) {
                        ComposedTextById myComposedTextById = (ComposedTextById) this.composedTextHashMap.get(MESS);
                        int num = Integer.parseInt(message.getArgument(0));
                        String mess = message.getArgument(1);
                        myComposedTextById.message.set(num, mess);
                        if (num >= myComposedTextById.size - 1) {
                            serverClient.notify(myComposedTextById.id, myComposedTextById);
                        }
                    }
                    break;
                case MESS:
                    if (message.getOperator() == Message.Operator.ASK) {
                        String id = message.getArgument(0);
                        String numMess = message.getArgument(1);
                        if (serverClient.cliManager.hasClientById(id)) {
                            ComposedTextById composedTextById = new ComposedTextById(id, Integer.parseInt(numMess));
                            composedTextHashMap.put(MESS, composedTextById);
                        }
                    }
                    break;
                case FLOO:
                    break;
                case LIST:
                    if (message.getOperator() == Message.Operator.ASK) {
                        int size = serverClient.cliManager.size();
                        soHandler.sendMessage(new Message(Message.Type.RLIST).addArgument(String.valueOf(size)));
                        for (int i = 0; i < size; i++) {
                            Client client1 = serverClient.cliManager.get(i);
                            soHandler.sendMessage(new Message(Message.Type.LINUM).addArgument(client1.getId()));
                        }
                    }
                    break;
                case CONSU:
                    break;
                case IQUIT:
                    soHandler.sendMessage(new Message(Message.Type.GOBYE));
                    stop();
                    break;
            }
        } catch (Exception e) {
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
        serverClient.removeClientHandler(this);
    }

}