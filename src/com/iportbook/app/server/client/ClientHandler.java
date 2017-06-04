package com.iportbook.app.server.client;

import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler extends ClientHandlerAbstract {

    public ClientHandler(ClientManager cliManager, Socket socket) throws IOException {
        super(socket, cliManager);
    }

    @Override
    protected void regis(String id, int password, int port) throws Exception {
        cliManager.addClient(id, password, port);
        soHandler.send(new MessageProcessor("WELCO").build());
    }

    @Override
    protected void conne(String id, int password) throws Exception {
        client = cliManager.getClient(id, password);
        soHandler.send(new MessageProcessor("HELLO").build());
    }

    @Override
    protected void mess(String id, int numMess) throws Exception {
        Flux flux = new Flux(3, new MessageProcessor("SSEM>").setId(client.getId()).setNumMess(numMess).build());
        for (int i = 0; i < numMess; i++) {
            MessageProcessor partial = new MessageProcessor(soHandler.read());
            String type = partial.getType();
            if (Objects.equals(type, "MENUM")) {
                //TODO: verification
                int num = partial.getNumMess();
                String mess = partial.getMess();
                flux.addPartial(new MessageProcessor("MUNEM").setNumMess(num).setMess(mess).build());
            }
        }
        cliManager.addFlux(id, flux);
    }

    @Override
    protected void floo(String mess) throws Exception {
        Flux flux = new Flux(4, new MessageProcessor("OOLF>").setId(client.getId()).setMess(mess).build());
        cliManager.floodFriend(client, flux);
        soHandler.send(new MessageProcessor("FLOO>").build());
    }

    @Override
    protected void frie(String id) throws Exception {
        try {
            if (Objects.equals(client.getId(), id) || !cliManager.hasClientById(id))
                throw new Exception();
            cliManager.addFlux(id, new Flux(0, new MessageProcessor("EIRF>").setId(client.getId()).build()));
            soHandler.send(new MessageProcessor("FRIE>").build());

        } catch (Exception e) {
            soHandler.send(new MessageProcessor("FRIE<").build());
        }
    }

    @Override
    protected void consu() throws Exception {
        Flux flux = client.popFlux();
        if (flux == null) {
            soHandler.send(new MessageProcessor("NOCON").build());
        } else {
            soHandler.send(flux.getMessage());
            MessageProcessor fluxMessage = new MessageProcessor(flux.getMessage());
            if (flux.hasPartials())
                for (int i = 0; i < flux.getPartialSize(); i++)
                    soHandler.send(flux.getPartial(i));
            switch (fluxMessage.getType()) {
                case "EIRF>": {
                    Flux fluxAnswer = new Flux(new MessageProcessor("ACKRF").build());
                    String id = fluxMessage.getId();
                    MessageProcessor receiveMessage = new MessageProcessor(soHandler.read());
                    switch (receiveMessage.getType()) {
                        case "OKIRF":
                            cliManager.addFriendship(id, client.getId());
                            fluxAnswer.setType(1).addPartial(new MessageProcessor("FRIEN").setId(client.getId()).build());
                            break;
                        case "NOKRF":
                            fluxAnswer.setType(2).addPartial(new MessageProcessor("NOFRI").setId(client.getId()).build());
                    }
                    cliManager.addFlux(id, fluxAnswer);
                }
            }
        }
    }

    @Override
    protected void list() throws Exception {
        int size = cliManager.getClientSize();
        soHandler.send(new MessageProcessor("RLIST").setNumItem(size).build());
        for (int i = 0; i < size; i++)
            soHandler.send(new MessageProcessor("LINUM").setId(cliManager.getClient(i).getId()).build());
    }

    @Override
    protected void iquit() throws Exception {
        soHandler.send(new MessageProcessor("GOBYE").build());
        stop();
    }
}