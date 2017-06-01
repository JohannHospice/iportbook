package com.iportbook.app.server.client;

import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.Tools;
import com.iportbook.core.tools.message.MessageTCP;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;
import java.net.Socket;

import static com.iportbook.core.tools.message.MessageTCP.Type;

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
            MessageTCP message = soHandler.receiveMessage();
            switch (message.getType()) {
                case CONNE:
                    if (message.getArgumentSize() != 2)
                        throw new Exception();
                    conne(
                            message.getArgument(0),
                            Tools.byteArrayToInt(message.getArgument(1).getBytes()));
                    break;
                case REGIS:
                    if (message.getArgumentSize() != 3)
                        throw new Exception();
                    regis(
                            message.getArgument(0),
                            Tools.byteArrayToInt(message.getArgument(2).getBytes()),
                            Integer.parseInt(message.getArgument(1)));
                    break;
                default:
                    stop();
                    break;
            }
        } catch (ClientException e) {
            try {
                soHandler.sendMessage(new MessageTCP(Type.GOBYE));
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
            switch (message.getType()) {
                case FRIE:
                    if (message.getArgumentSize() == 1 && message.getOperator() == MessageTCP.Operator.ASK)
                        frie(message.getArgument(0));
                    break;
                case MESS:
                    if (message.getOperator() == MessageTCP.Operator.ASK && message.getArgumentSize() == 2)
                        mess(message.getArgument(0), Integer.parseInt(message.getArgument(1)));
                    break;
                case LIST:
                    if (message.getOperator() == MessageTCP.Operator.ASK)
                        list();
                    break;
                case FLOO:
                    if (message.getOperator() == MessageTCP.Operator.ASK)
                        floo(message.getArgument(0));
                    break;
                case CONSU:
                    consu();
                    break;
                case IQUIT:
                    iquit();
                    break;
                default:
                    throw new Exception();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            stop();
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

    protected abstract void regis(String id, int password, int port) throws Exception;

    protected abstract void conne(String id, int password) throws Exception;

    protected abstract void mess(String id, int numMess) throws Exception;

    protected abstract void frie(String id) throws Exception;

    protected abstract void floo(String mess) throws Exception;

    protected abstract void consu() throws Exception;

    protected abstract void list() throws Exception;

    protected abstract void iquit() throws Exception;

}