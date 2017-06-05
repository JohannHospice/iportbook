package com.iportbook.app.server.client;

import com.iportbook.app.ClientAction;
import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public abstract class ClientHandlerAbstract extends ApplicationListener implements ClientAction {
    protected final ClientManager cliManager = ClientManager.getInstance();
    protected final DataSocket daSo;
    protected Client client;

    protected ClientHandlerAbstract(Socket socket) throws IOException {
        this.daSo = new DataSocket(socket);
    }

    @Override
    protected void onStart() {
        try {
            MessageProcessor message = new MessageProcessor(daSo.read());
            String type = message.getType();
            String id = message.getId();
            switch (type) {
                case "CONNE": {
                    int pwd = message.getPassword();
                    conne(id, pwd);
                    break;
                }
                case "REGIS": {
                    int port = message.getPort();
                    int pwd = message.getPassword();
                    regis(id, pwd, port);
                    break;
                }
                default:
                    stop();
                    break;
            }
        } catch (ClientException e) {
            try {
                daSo.send(new MessageProcessor("GOBYE").build());
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
            MessageProcessor message = new MessageProcessor(daSo.read());
            String type = message.getType();
            switch (type) {
                case "FRIE?": {
                    String id = message.getId();
                    frie(id);
                    break;
                }
                case "MESS?": {
                    String id = message.getId();
                    int numMess = message.getNumMess();
                    mess(id, numMess);
                    break;
                }
                case "FLOO?": {
                    String mess = message.getMess();
                    floo(mess);
                    break;
                }
                case "LIST?":
                    list();
                    break;
                case "CONSU":
                    consu();
                    break;
                case "IQUIT":
                    iquit();
                    break;
                default:
                    throw new Exception();
            }
        } catch (SocketException e) {
            e.printStackTrace();
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnd() {
        try {
            daSo.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        cliManager.removeClientHandler(this);
    }

}