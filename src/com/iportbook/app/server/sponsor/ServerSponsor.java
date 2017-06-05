package com.iportbook.app.server.sponsor;

import com.iportbook.app.server.ServerListener;
import com.iportbook.app.server.client.ClientManager;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ServerSponsor extends ServerListener {

    private ArrayList<SponsorHandler> sponsorHandlers = new ArrayList<>();

    public ServerSponsor(int port) {
        super(port);
    }

    @Override
    protected void onAccept(Socket accept) throws IOException {
        SponsorHandler sponsorHandler = new SponsorHandler(accept);
        sponsorHandlers.add(sponsorHandler);
        new Thread(sponsorHandler).start();
    }

    @Override
    protected void onEnd() {
        for (SponsorHandler spo : sponsorHandlers)
            spo.stop();
        super.onEnd();
    }

    public class SponsorHandler extends ApplicationListener {
        private final DataSocket daSo;

        SponsorHandler(Socket socket) throws IOException {
            this.daSo = new DataSocket(socket);
        }

        @Override
        protected void onStart() throws Exception {
        }

        @Override
        protected void onLoop() throws Exception {
            try {
                MessageProcessor messageProcessor = daSo.readMessageProcessor();
                String type = messageProcessor.getType();
                if (Objects.equals(type, "PUBL?")) {
                    String ipDiff = messageProcessor.getIpDiff();
                    int port = messageProcessor.getPort();
                    String mess = messageProcessor.getMess();
                    publ(ipDiff, port, mess);
                }
            } catch (IOException e) {
                stop();
            } catch (Exception ignored) {
            }
        }

        @Override
        protected void onEnd() throws Exception {
            daSo.close();
            sponsorHandlers.remove(this);
        }

        private void publ(String ipDiff, int port, String mess) throws Exception {
            ClientManager.getInstance().addFluxToAll(Flux.PUBL, new MessageProcessor("LBUP>").setIpDiff(ipDiff).setPort(port).setMess(mess).build());
            daSo.send(new MessageProcessor("PUBL>").build());
        }

        @Override
        public void stop() {
            super.stop();
            try {
                daSo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
