package com.iportbook.client;

import java.util.Scanner;
import java.util.logging.Logger;

public abstract class ClientAbstract implements Runnable{
    private static final Logger logger = Logger.getLogger(String.valueOf(ClientAbstract.class));
    private final int port;
    private String id;
    public ClientAbstract(int port){
        this.port = port;
    }

    public abstract void login(String password);
    public abstract void logout();
    public abstract void register(String password);
    public abstract void friend(String idTo);
    public abstract void message(String idTo);
    public abstract void flood();
    public abstract void list();
    public abstract void consultation();
}
