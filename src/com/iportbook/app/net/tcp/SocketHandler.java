package com.iportbook.app.net.tcp;

import java.io.*;
import java.net.Socket;

public class SocketHandler {
    private final Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public SocketHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void send(String text) throws IOException {
        getPw().print(text);
        getPw().flush();
    }

    public String receive() throws IOException {
        return getBr().readLine();
    }

    public void close() throws IOException {
        if (pw != null)
            pw.close();
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

    private PrintWriter getPw() throws IOException {
        if (pw == null)
            this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        return pw;
    }
}
