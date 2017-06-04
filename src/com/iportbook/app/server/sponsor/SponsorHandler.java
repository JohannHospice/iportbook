package com.iportbook.app.server.sponsor;

import com.iportbook.app.server.client.ClientManager;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;

public class SponsorHandler extends ApplicationListener {
    private final DataSocket daSo;
    private final ClientManager cliManager = ClientManager.getInstance();

    SponsorHandler(Socket socket) throws IOException {
        this.daSo = new DataSocket(socket);
    }

    @Override
    protected void onStart() throws Exception {
    }

    @Override
    protected void onLoop() throws Exception {
        MessageProcessor messageProcessor = new MessageProcessor(daSo.read());
        String type = messageProcessor.getType();
        switch (type) {
            /*
            case "PROM":{
                String promMess = messageProcessor.getPromMess();
                break;
            }
            */
            case "PUBL?": {
                String ipDiff = messageProcessor.getIpDiff();
                int port = messageProcessor.getPort();
                String mess = messageProcessor.getMess();
                cliManager.addFluxToAll(Flux.PUBL, new MessageProcessor("LBUP>").setIpDiff(ipDiff).setPort(port).setMess(mess).build());
                daSo.send(new MessageProcessor("PUBL>").build());
                break;
            }
        }
    }

    @Override
    protected void onEnd() throws Exception {
        daSo.close();
    }
}
