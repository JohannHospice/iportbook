package com.iportbook.app.client;

import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.util.Objects;

public class AppClient extends AppClientAbstract {

    private static final int SIZE_MESS = 200;

    private AppClient(String host, int portTCP, int portUDP) throws IOException {
        super(host, portTCP, portUDP);
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: need 2 arguments <portTCP> <portUDP>");
            return;
        }
        final String hostTCP = "localhost";
        final int portTCP = Integer.parseInt(args[0]);
        final int portUDP = Integer.parseInt(args[1]);

        AppClient appClient = new AppClient(hostTCP, portTCP, portUDP);
        new Thread(appClient).start();
    }

    @Override
    void abo(String host, int port) throws IOException {
        SponsorHandler sponsorHandler = new SponsorHandler(host, port);
        sponsorHandlers.add(sponsorHandler);
        new Thread(sponsorHandler).start();
        System.out.println("Vous etes maintenant abonné au promoteur #" + host + ":" + port + "");
    }

    @Override
    public void regis(String id, int password, int port) throws Exception {
        daSo.send(new MessageProcessor("REGIS").setId(id).setPort(port).setPassword(password).build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        switch (type) {
            case "WELCO":
                this.id = id;
                this.port = port;
                System.out.println("vous etes bien inscrit");
                break;
            case "GOBYE":
                System.err.println("erreur lors de l'inscription");
            default:
                throw new Exception();
        }
    }

    @Override
    public void conne(String id, int password) throws Exception {
        daSo.send(new MessageProcessor("CONNE").setId(id).setPassword(password).build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        switch (type) {
            case "HELLO":
                this.id = id;
                System.out.println("vous etes bien connecté");
                break;
            case "GOBYE":
                System.err.println("erreur lors de la connection");
            default:
                throw new Exception();
        }
    }

    @Override
    public void mess(String id, String mess) throws Exception {
        int numMess = (int) Math.ceil(((double) mess.length()) / SIZE_MESS);
        daSo.send(new MessageProcessor("MESS?").setId(id).setNumMess(numMess).build());
        for (int i = 0; i < numMess; i++) {
            int offset = i * SIZE_MESS;
            int limit = (i + 1) * SIZE_MESS;
            String subMess = mess.substring(offset, limit > mess.length() ? mess.length() : limit);
            daSo.send(new MessageProcessor("MENUM").setNumMess(i).setMess(subMess).build());
        }
        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        switch (type) {
            case "MESS>":
                System.out.println("Le message a bien été retransmis à @" + id);
                break;
            case "MESS<":
                System.err.println("Le message n'a pas pu être retransmis à @" + id);
                break;
        }
    }

    @Override
    public void frie(String id) throws Exception {
        daSo.send(new MessageProcessor("FRIE?").setId(id).build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        switch (type) {
            case "FRIE>":
                System.out.println("La demande d'ami a bien été retransmise");
                break;
            case "FRIE<":
                System.err.println("La demande d'ami n'a pas pu etre retransmise");
                break;
            default:
                System.err.println("error");
        }
    }

    @Override
    public void floo(String mess) throws Exception {
        daSo.send(new MessageProcessor("FLOO?").setMess(mess).build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();

        if (Objects.equals("FLOO>", type))
            System.out.println("Le flood a bien été retransmis");
        else
            System.err.println("Le flood n'a pas pu être retransmis");
    }

    @Override
    public void consu() throws Exception {
        daSo.send(new MessageProcessor("CONSU").build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        switch (type) {
            case "NOCON":
                System.out.println("Aucune nouvelles notifications");
                break;
            case "SSEM>": {
                String id = messageProcessor.getId();
                int numMess = messageProcessor.getNumMess();
                StringBuilder output = new StringBuilder();
                output.append(id).append(": ");
                for (int i = 0; i < numMess; i++) {
                    MessageProcessor partial = daSo.readMessageProcessor();
                    String typePartial = partial.getType();
                    if (Objects.equals("MUNEM", typePartial)) {
                        int num = partial.getNumMess();
                        String mess = partial.getMess();
                        output.insert(output.length() + num * SIZE_MESS, mess);
                    }
                }
                System.out.println(output.toString());
                break;
            }
            case "OOLF>": {
                String id = messageProcessor.getId();
                String mess = messageProcessor.getMess();
                System.out.println(id + ": " + mess);
                break;
            }
            case "LBUP>": {
                String ipDiff = messageProcessor.getIpDiff();
                int port = messageProcessor.getPort();
                String mess = messageProcessor.getMess();
                System.out.println("#" + ipDiff + ":" + port + " publ: " + mess);
                break;
            }
            case "EIRF>": {
                String id = messageProcessor.getId();
                System.out.println("@" + id + " vous demande en ami");
                String input = termScanner.askNext("Voulez vous accepté sa demande?\n0:\tnon\n1:\toui", "^(0|1)$");
                switch (input.charAt(0)) {
                    case '1': {
                        daSo.send(new MessageProcessor("OKIRF").build());
                        break;
                    }
                    case '0': {
                        daSo.send(new MessageProcessor("NOKFR").build());
                        break;
                    }
                }
                MessageProcessor answer = daSo.readMessageProcessor();
                String typeAnswer = answer.getType();
                if (Objects.equals("ACKRF", typeAnswer))
                    System.out.println("Votre réponse a été prise en consideration");
                break;
            }
            case "FRIEN": {
                String id = messageProcessor.getId();
                System.out.println("@" + id + " a accepté votre demande d'ami");
                break;
            }
            case "NOFRI": {
                String id = messageProcessor.getId();
                System.err.println("@" + id + " a refusé votre demande d'ami");
                break;
            }
        }
    }

    @Override
    public void list() throws Exception {
        daSo.send(new MessageProcessor("LIST?").build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();

        if (Objects.equals(type, "RLIST")) {
            int numItem = messageProcessor.getNumItem();
            System.out.println("Voici la liste des " + numItem + " clients");
            for (int i = 0; i < numItem; i++) {
                MessageProcessor partial = daSo.readMessageProcessor();
                String partialType = partial.getType();
                if (Objects.equals(partialType, "LINUM")) {
                    String partialId = partial.getId();
                    System.out.println(" - " + partialId);
                }
            }
        }
    }

    @Override
    public void iquit() throws Exception {
        daSo.send(new MessageProcessor("IQUIT").build());

        MessageProcessor messageProcessor = daSo.readMessageProcessor();
        String type = messageProcessor.getType();
        if (Objects.equals(type, "GOBYE")) {
            System.out.println("Vous etes deconnectes du serveur");
            stop();
        }
    }
}
