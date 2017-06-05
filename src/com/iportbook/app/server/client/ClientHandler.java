package com.iportbook.app.server.client;

import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler extends ClientHandlerAbstract {

    public ClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void regis(String id, int password, int port) throws Exception {
            client = cliManager.addClient(id, password, port);
            daSo.send(new MessageProcessor("WELCO").build());
    }

    @Override
    public void conne(String id, int password) throws Exception {
            client = cliManager.getClient(id, password);
            daSo.send(new MessageProcessor("HELLO").build());
    }

    @Override
    public void mess(String id, int numMess) throws Exception {
        try {
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
            daSo.send(new MessageProcessor("MESS>").build());
        } catch (Exception e) {
            daSo.send(new MessageProcessor("MESS<").build());
            e.printStackTrace();
        }
    }


    @Override
    public void floo(String mess) throws Exception {
        Flux flux = new Flux(4, new MessageProcessor("OOLF>").setId(client.getId()).setMess(mess).build());
        cliManager.floodFriend(client, flux);
        daSo.send(new MessageProcessor("FLOO>").build());
    }

    @Override
    public void frie(String id) throws Exception {
        try {
            if (Objects.equals(client.getId(), id) || !cliManager.hasClientById(id))
                throw new Exception();
            cliManager.addFlux(id, new Flux(Flux.FRIE, new MessageProcessor("EIRF>").setId(client.getId()).build()));
            daSo.send(new MessageProcessor("FRIE>").build());
        } catch (Exception e) {
            daSo.send(new MessageProcessor("FRIE<").build());
        }
    }

    @Override
    public void consu() throws Exception {
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
                            cliManager.addFriendship(id, client.getId());
                            cliManager.addFlux(id, new Flux(Flux.OKIRF, new MessageProcessor("FRIEN").setId(client.getId()).build()));
                            break;
                        }
                        case "NOKRF": {
                            cliManager.addFlux(id, new Flux(Flux.NOKRF, new MessageProcessor("NOFRI").setId(client.getId()).build()));
                        }
                    }
                    daSo.send(new MessageProcessor("ACKRF").build());
                }
            }
        }
    }

    @Override
    public void list() throws Exception {
        int size = cliManager.getClientSize();
        daSo.send(new MessageProcessor("RLIST").setNumItem(size).build());
        for (int i = 0; i < size; i++)
            daSo.send(new MessageProcessor("LINUM").setId(cliManager.getClient(i).getId()).build());
    }

    @Override
    public void iquit() throws Exception {
        daSo.send(new MessageProcessor("GOBYE").build());
        stop();
    }
}