package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;

public abstract class ClientHandlerAbstract extends ApplicationListener {
    protected final ClientManager cliManager = ClientManager.getInstance();
    protected final DataSocket daSo;
    protected Client client;

    protected ClientHandlerAbstract(Socket socket) throws IOException {
        this.daSo = new DataSocket(socket);
    }

    @Override
    protected void onStart() {
        try {
            MessageProcessor message = daSo.readMessageProcessor();
            if (ClientManager.getInstance().getClientSize() >= ClientManager.MAX_CLI_HANDLER)
                throw new Exception();
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
        } catch (Exception e) {
            try {
                daSo.send(new MessageProcessor("GOBYE").build());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            stop();
        }
    }

    @Override
    protected void onLoop() {
        try {
            MessageProcessor message = daSo.readMessageProcessor();
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
        } catch (IOException e) {
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

    abstract void regis(String id, int password, int port) throws Exception;

    abstract void conne(String id, int password) throws Exception;

    abstract void mess(String id, int numMess) throws Exception;

    abstract void frie(String id) throws Exception;

    abstract void floo(String mess) throws Exception;

    abstract void consu() throws Exception;

    abstract void list() throws Exception;

    abstract void iquit() throws Exception;
}