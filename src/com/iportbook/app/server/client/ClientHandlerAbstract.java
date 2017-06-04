package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.SocketHandler;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.net.Socket;

public abstract class ClientHandlerAbstract extends ApplicationListener {
    protected final ClientManager cliManager;
    protected final SocketHandler soHandler;
    protected Client client;

    protected ClientHandlerAbstract(Socket socket, ClientManager cliManager) throws IOException {
        this.soHandler = new SocketHandler(socket);
        this.cliManager = cliManager;
    }

    @Override
    protected void onStart() {
        try {
            MessageProcessor message = new MessageProcessor(soHandler.read());
            String id = message.getId();
            switch (message.getType()) {
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
                soHandler.send(new MessageProcessor("GOBYE").close());
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
            MessageProcessor message = new MessageProcessor(soHandler.read());
            switch (message.getType()) {
                case "FRIE?":
                    frie(message.getId());
                    break;
                case "MESS?":
                    mess(message.getId(), message.getNumMess());
                    break;
                case "LIST?":
                    list();
                    break;
                case "FLOO?":
                    floo(message.getMess());
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
        } /*catch (NullPointerException e) {
            e.printStackTrace();
            stop();
        } */ catch (Exception e) {
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

    protected abstract void regis(String id, int password, int port) throws Exception;

    protected abstract void conne(String id, int password) throws Exception;

    protected abstract void mess(String id, int numMess) throws Exception;

    protected abstract void frie(String id) throws Exception;

    protected abstract void floo(String mess) throws Exception;

    protected abstract void consu() throws Exception;

    protected abstract void list() throws Exception;

    protected abstract void iquit() throws Exception;

}