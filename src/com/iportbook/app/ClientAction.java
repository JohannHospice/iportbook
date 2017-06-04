package com.iportbook.app;

public interface ClientAction {

    void regis(String id, int password, int port) throws Exception;

    void conne(String id, int password) throws Exception;

    void mess(String id, int numMess) throws Exception;

    void frie(String id) throws Exception;

    void floo(String mess) throws Exception;

    void consu() throws Exception;

    void list() throws Exception;

    void iquit() throws Exception;
}
