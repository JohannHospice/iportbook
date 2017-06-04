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
        daSo.send(new MessageProcessor("WELCO").build());
    }

    @Override
    protected void conne(String id, int password) throws Exception {
        client = cliManager.getClient(id, password);
        daSo.send(new MessageProcessor("HELLO").build());
    }

    @Override
    protected void mess(String id, int numMess) throws Exception {
        Flux flux = new Flux(3, new MessageProcessor("SSEM>").setId(client.getId()).setNumMess(numMess).build());
        for (int i = 0; i < numMess; i++) {
            MessageProcessor partial = new MessageProcessor(daSo.read());
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
        daSo.send(new MessageProcessor("FLOO>").build());
    }

    @Override
    protected void frie(String id) throws Exception {
        try {
            if (Objects.equals(client.getId(), id) || !cliManager.hasClientById(id))
                throw new Exception();
            cliManager.addFlux(id, new Flux(0, new MessageProcessor("EIRF>").setId(client.getId()).build()));
            daSo.send(new MessageProcessor("FRIE>").build());

        } catch (Exception e) {
            daSo.send(new MessageProcessor("FRIE<").build());
        }
    }

    @Override
    protected void consu() throws Exception {
        Flux flux = client.popFlux();
        if (flux == null) {
            daSo.send(new MessageProcessor("NOCON").build());
        } else {
            daSo.send(flux.getMessage());
            MessageProcessor fluxMessage = new MessageProcessor(flux.getMessage());
            if (flux.hasPartials())
                for (int i = 0; i < flux.getPartialSize(); i++)
                    daSo.send(flux.getPartial(i));
            switch (fluxMessage.getType()) {
                case "EIRF>": {
                    String id = fluxMessage.getId();
                    MessageProcessor receiveMessage = new MessageProcessor(daSo.read());
                    String type = receiveMessage.getType();
                    switch (type) {
                        case "OKIRF": {
                            daSo.send(new MessageProcessor("ACKRF").build());
                            cliManager.addFriendship(id, client.getId());
                            cliManager.addFlux(id, new Flux(1, new MessageProcessor("FRIEN").setId(client.getId()).build()));
                            break;
                        }
                        case "NOKRF": {
                            daSo.send(new MessageProcessor("ACKRF").build());
                            cliManager.addFlux(id, new Flux(2, new MessageProcessor("NOFRI").setId(client.getId()).build()));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void list() throws Exception {
        int size = cliManager.getClientSize();
        daSo.send(new MessageProcessor("RLIST").setNumItem(size).build());
        for (int i = 0; i < size; i++)
            daSo.send(new MessageProcessor("LINUM").setId(cliManager.getClient(i).getId()).build());
    }

    @Override
    protected void iquit() throws Exception {
        daSo.send(new MessageProcessor("GOBYE").build());
        stop();
    }
}