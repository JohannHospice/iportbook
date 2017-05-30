package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.Tools;
import com.iportbook.core.tools.net.SocketHandler;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.composed.CompoundMessage;
import com.iportbook.core.tools.composed.CompoundMessageById;
import com.iportbook.core.tools.message.MessageTCP;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import static com.iportbook.core.tools.message.MessageTCP.Type.MESS;

public class ClientHandler extends ApplicationListener {
    private HashMap<MessageTCP.Type, CompoundMessage> messageWaiting = new HashMap<>();
    private final ClientManager cliManager;
    private final SocketHandler soHandler;
    private Client client;

    public ClientHandler(ClientManager cliManager, Socket socket) throws IOException {
        this.cliManager = cliManager;
        this.soHandler = new SocketHandler(socket);
    }

    @Override
    protected void onStart() {
        try {
            MessageTCP message = soHandler.receiveMessage();
            LOGGER.info("received message: " + message);
            switch (message.getType()) {
                case CONNE:
                    conne(message);
                    break;
                case REGIS:
                    regis(message);
                    break;
                default:
                    stop();
                    break;
            }
        } catch (ClientException e) {
            try {
                soHandler.sendMessage(new MessageTCP(MessageTCP.Type.GOBYE));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            stop();
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    protected void onLoop() {
        try {
            MessageTCP message = soHandler.receiveMessage();
            LOGGER.info("receiveby/" + client.getId() + ": " + message);
            handleLogon(message);
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
        cliManager.removeClientHandler(this);
    }

    private void handleLogon(MessageTCP message) throws Exception {
        switch (message.getType()) {
            case FRIE:
                frie(message);
                break;
            case MENUM:
                menum(message);
                break;
            case MESS:
                mess(message);
                break;
            case FLOO:
                floo(message);
                break;
            case LIST:
                list(message);
                break;
            case CONSU:
                consu(message);
                break;
            case IQUIT:
                iquit();
                break;
        }
    }

    private void regis(MessageTCP message) throws Exception {
        if (message.getArgumentSize() != 3)
            throw new Exception();
        client = new Client(
                message.getArgument(0),
                Tools.byteArrayToInt(message.getArgument(2).getBytes()),
                Integer.parseInt(message.getArgument(1)));
        cliManager.addClient(client);
        soHandler.sendMessage(new MessageTCP(MessageTCP.Type.WELCO));
    }

    private void conne(MessageTCP message) throws Exception {
        if (message.getArgumentSize() != 2)
            throw new Exception();
        client = cliManager.getClient(
                message.getArgument(0),
                message.getArgument(1));
        soHandler.sendMessage(new MessageTCP(MessageTCP.Type.HELLO));
    }

    private void mess(MessageTCP message) throws Exception {
        if (message.getArgumentSize() != 2 || message.getOperator() != MessageTCP.Operator.ASK)
            throw new Exception();
        String id = message.getArgument(0);
        int numMess = Integer.parseInt(message.getArgument(1));
        if (cliManager.hasClientById(id))
            messageWaiting.put(MESS, new CompoundMessageById(id, numMess));
    }

    private void menum(MessageTCP message) throws Exception {
        if (message.getArgumentSize() != 2 || !messageWaiting.containsKey(MESS))
            throw new Exception();
        CompoundMessageById compoundMessage = (CompoundMessageById) this.messageWaiting.get(MESS);
        int num = Integer.parseInt(message.getArgument(0));
        compoundMessage.setMessage(num, message.getArgument(1));
        if (num >= compoundMessage.getSize() - 1) {
            cliManager.addFluxToClient(compoundMessage.getId(), new Flux(3));
            messageWaiting.remove(MESS);
        }
    }

    private void list(MessageTCP message) throws Exception {
        if (message.getOperator() != MessageTCP.Operator.ASK)
            throw new Exception();
        int size = cliManager.getClientSize();
        soHandler.sendMessage(new MessageTCP(MessageTCP.Type.RLIST).addArgument(String.valueOf(size)));
        for (int i = 0; i < size; i++) {
            Client client1 = cliManager.getClient(i);
            soHandler.sendMessage(new MessageTCP(MessageTCP.Type.LINUM).addArgument(client1.getId()));
        }
    }

    private void frie(MessageTCP message) throws Exception {
        if (message.getArgumentSize() != 1 || message.getOperator() != MessageTCP.Operator.ASK)
            throw new Exception();
        String id = message.getArgument(0);
        if (cliManager.hasClientById(id)) {
            soHandler.sendMessage(new MessageTCP(MessageTCP.Type.FRIE, MessageTCP.Operator.CRIGHT));
            cliManager.addFluxToClient(id, new Flux(0, client.getId()));
        } else
            soHandler.sendMessage(new MessageTCP(MessageTCP.Type.FRIE, MessageTCP.Operator.CLEFT));
    }

    private void floo(MessageTCP message) throws Exception {
        throw new Exception();
    }

    private void consu(MessageTCP message) throws Exception {
        throw new Exception();
    }

    private void iquit() throws Exception {
        soHandler.sendMessage(new MessageTCP(MessageTCP.Type.GOBYE));
        stop();
    }
}