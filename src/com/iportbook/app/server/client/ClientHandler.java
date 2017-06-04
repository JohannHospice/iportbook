package com.iportbook.app.server.client;

import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends ClientHandlerAbstract {

    public ClientHandler(ClientManager cliManager, Socket socket) throws IOException {
        super(socket, cliManager);
    }

    @Override
    protected void regis(String id, int password, int port) throws Exception {
        cliManager.addClient(id, password, port);
        soHandler.send(new MessageProcessor("WELCO").close());
    }

    @Override
    protected void conne(String id, int password) throws Exception {
        client = cliManager.getClient(id, password);
        soHandler.send(new MessageProcessor("HELLO").close());
    }

    @Override
    protected void mess(String id, int numMess) throws Exception {
        /*
        Flux flux = new Flux(3, new MessageTCP(Type.SSEM, MessageTCP.Operator.CRIGHT)
                .addArguments(client.getId(), String.valueOf(numMess)));
        for (int i = 0; i < numMess; i++) {
            MessageTCP partial = soHandler.receiveMessage();
            if (partial.getType() == Type.MENUM && partial.getArgumentSize() == 2) {
                //TODO: verification
                String num = partial.getArgument(0);
                String mess = partial.getArgument(1);
                flux.addPartial(new MessageTCP(Type.MUNEM).addArguments(num, mess));
            }
        }
        cliManager.addFlux(id, flux);
        */
    }

    @Override
    protected void floo(String mess) throws Exception {
        /*
        Flux flux = new Flux(4, new MessageTCP(Type.OOLF, Operator.CRIGHT)
                .addArguments(client.getId(), mess));
        cliManager.floodFriend(client, flux);
        soHandler.sendMessage(new MessageTCP(Type.FLOO, Operator.CRIGHT));
        */
    }

    @Override
    protected void frie(String id) throws Exception {
        /*
        try {
            if (Objects.equals(client.getId(), id) || !cliManager.hasClientById(id))
                throw new Exception();
            cliManager.addFlux(id, new Flux(0, new MessageTCP(Type.EIRF, Operator.CRIGHT).addArgument(client.getId())));
            soHandler.sendMessage(new MessageTCP(Type.FRIE, Operator.CRIGHT));

        } catch (Exception e) {
            soHandler.sendMessage(new MessageTCP(Type.FRIE, Operator.CLEFT));
        }
        */
    }

    @Override
    protected void consu() throws Exception {
        /*
        Flux flux = client.popFlux();
        if (flux == null) {
            soHandler.sendMessage(new MessageTCP(Type.NOCON));
        } else {
            MessageTCP fluxMessage = flux.getMessage();
            soHandler.sendMessage(fluxMessage);
            if (flux.hasPartials())
                for (int i = 0; i < flux.getPartialSize(); i++)
                    soHandler.sendMessage(flux.getPartial(i));
            switch (fluxMessage.getType()) {
                case EIRF:
                    if (fluxMessage.getOperator() == Operator.CRIGHT && fluxMessage.getArgumentSize() == 1) {
                        Flux fluxAnswer = new Flux(new MessageTCP(Type.ACKRF));
                        String id = fluxMessage.getArgument(0);
                        MessageTCP receiveMessage = soHandler.receiveMessage();
                        switch (receiveMessage.getType()) {
                            case OKIRF:
                                cliManager.addFriendship(id, client.getId());
                                fluxAnswer.setType(1).addPartial(new MessageTCP(Type.FRIEN).addArguments(client.getId()));
                                break;
                            case NOKRF:
                                fluxAnswer.setType(2).addPartial(new MessageTCP(Type.NOFRI).addArguments(client.getId()));
                        }
                        cliManager.addFlux(id, fluxAnswer);
                    }
            }
        }
        */
    }

    @Override
    protected void list() throws Exception {
        int size = cliManager.getClientSize();
        soHandler.send(new MessageProcessor("RLIST").setNumItem(size).close());
        for (int i = 0; i < size; i++)
            soHandler.send(new MessageProcessor("LINUM").setId(cliManager.getClient(i).getId()).close());
    }

    @Override
    protected void iquit() throws Exception {
        soHandler.send(new MessageProcessor("GOBYE").close());
        stop();
    }
}