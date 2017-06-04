package com.iportbook.app.client;

import com.iportbook.app.ClientAction;
import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DataSocket;
import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.IOException;
import java.util.Scanner;

public class AppClient extends ApplicationListener implements ClientAction {
    private final Scanner scanner;
    private final String host;
    private int portTCP;
    /**
     * allow you to send and receive TCP request
     */
    private DataSocket daSo;
    private NotificationHandler notificationHandler;

    private AppClient(String host, int portTCP, int portUDP) throws IOException {
        this.host = host;
        this.portTCP = portTCP;
        scanner = new Scanner(System.in);
        notificationHandler = new NotificationHandler(portUDP);
        daSo = new DataSocket();
    }

    @Override
    protected void onStart() throws Exception {
        // run udp notification handler
        new Thread(notificationHandler).start();
        // run tcp
        daSo.bind(host, portTCP);
        portTCP = daSo.getLocalPort();

        //conne regis
        char type = ask("connection(0), register(1): ", "\\d").charAt(0);
        String id = ask("identifiant: ");
        int password = Integer.parseInt(ask("password: "));

        switch (type) {
            case '0':
                conne(id, password);
                break;
            case '1':
                int port = notificationHandler.getPort();
                regis(id, password, port);
                break;
        }
    }

    @Override
    protected void onLoop() throws Exception {
        // first send
        String text = scanner.nextLine();
        daSo.send(inputToMessageProcessor(text).build());

        // then receive answer
        MessageProcessor received = new MessageProcessor(daSo.read());
        switch (received.getType()) {
            //TODO: each case of server answer (in relation with the message sent
        }
    }

    @Override
    protected void onEnd() throws Exception {
        daSo.close();
        scanner.close();
    }

    /**
     * get MessageTCP object by raw text
     * transformation has to be determine there
     *
     * @param input String
     * @return MessageTCP
     */
    // TODO: process an easiest protocol for text treatment

    private MessageProcessor inputToMessageProcessor(String input) throws Exception {
    	String firstChar = input.substring(0, 1);
    	MessageProcessor mp = null;

    	if (firstChar == "+") {
    		String arr[] = input.split(" ", 2);

    		switch (arr[0]) {
    				case "+list":
    					mp = new MessageProcessor("LIST?");
    					break;
    				case "+frie":
    					String id_fri = arr[1];
    					mp = new MessageProcessor("FRIE?").setId(id_fri);
    					break;
    				case "+quit":
    					mp = new MessageProcessor("IQUIT");
    					break;
    				default: {
    					String id_dest = arr[0].substring(1, arr[0].length());
    					mp = new MessageProcessor("MESS?").setId(id_dest).setMess(arr[1]);
    					break;
    			}
    		}
    	}
    	else if (!input.isEmpty()){
    		mp = new MessageProcessor("FLOO?").setMess(input);
    	}
    	return mp;
	}
    /**
     * get next text wrote on terminal with a specific pattern(regex)
     *
     * @param pattern String
     * @return String
     */
    private String getNext(String pattern) {
        String next = null;
        while (scanner.hasNext(pattern))
            next = scanner.next();
        return next;
    }

    public String ask(String question) {
        System.out.println(question);
        return scanner.nextLine();
    }

    public String ask(String question, String pattern) {
        System.out.println(question);
        return getNext(pattern);
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer portTCP");
            return;
        }
        AppClient appClient = new AppClient("localhost", Integer.parseInt(args[0]), 8383);
        new Thread(appClient).start();
    }

    //TODO
    @Override
    public void regis(String id, int password, int port) throws Exception {
    	daSo.send(new MessageProcessor("REGIS").setId(id).setPassword(password).setPort(port).build());
    	
    	MessageProcessor messageProcessor = new MessageProcessor(daSo.read());
    	String type = messageProcessor.getType();
    	switch(type) {
    		case "WELCO":
    			System.out.println("Utilisateur enregistree" + messageProcessor.getId() + " " + messageProcessor.getPassword());
    			break;
    		case "GOBYE":
    			System.out.println("Serveur rempli ou Mauvais port ou Mauvais password");
    			stop();
    		default:
    			System.out.println("Fatal Error Register");
    			stop();
    	}
    }

    @Override
    public void conne(String id, int password) throws Exception {
        daSo.send(new MessageProcessor("CONNE").setId(id).setPassword(password).build());

        MessageProcessor messageProcessor = new MessageProcessor(daSo.read());
        String type = messageProcessor.getType();
        switch (type) {
            case "HELLO":
                System.out.println("vous etes bien connecté");
                break;
            case "GODBY": {
                System.out.println("erreur lors de la connection");
                stop();
            }
            default: {
                System.err.println("fatal error");
                stop();
            }
        }
    }

    //TODO
    @Override
    public void mess(String id, int numMess) throws Exception {
    }

    //TODO
    @Override
    public void frie(String id) throws Exception {

    }

    //TODO
    @Override
    public void floo(String mess) throws Exception {

    }

    //TODO
    @Override
    public void consu() throws Exception {

    }

    //TODO
    @Override
    public void list() throws Exception {

    }

    //TODO
    @Override
    public void iquit() throws Exception {

    }
}
