package com.iportbook.core.tools.net;

import com.iportbook.core.tools.message.MessageTCP;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketHandler {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    public SocketHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void send(String text) throws IOException {
        LOGGER.info("send: [" + text + "]");
        getBw().write(text + '\n');
        getBw().flush();
    }

    public String receive() throws IOException {
        String text = getBr().readLine();
        if (null != text)
            LOGGER.info("receive: [" + text + "]");
        return text;
    }

    public MessageTCP receiveMessage() throws Exception {
        return MessageTCP.parse(receive());
    }

    public void sendMessage(MessageTCP message) throws Exception {
        send(message.compose());
    }

    public void close() throws IOException {
        if (bw != null)
            bw.close();
        if (br != null)
            br.close();
        socket.close();
    }

    public int getPort() {
        return socket.getPort();
    }

    private BufferedReader getBr() throws IOException {
        if (br == null)
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return br;
    }

    private BufferedWriter getBw() throws IOException {
        if (bw == null)
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        return bw;
    }
}
