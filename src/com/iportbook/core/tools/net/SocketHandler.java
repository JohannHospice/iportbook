package com.iportbook.core.tools.net;

import com.iportbook.core.tools.processor.MessageProcessor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class SocketHandler {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private static final int MAXLEN = 300;
    private final Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private DataOutputStream dos;
    private DataInputStream dis;

    public SocketHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public SocketHandler() throws IOException {
        this.socket = new Socket();
    }

    private static void dequeue(byte[] data, byte value) {
        for (int j = 0; j < data.length - 1; j++)
            data[j] = data[j + 1];
        data[data.length - 1] = value;
    }

    public void bind(String host, int port) throws IOException {
        this.socket.bind(new InetSocketAddress(host, port));
    }

    public void send(String text) throws IOException {
        LOGGER.info("send: [" + text + "]");
        getBw().write(text + '\n');
        getBw().flush();
    }

    public void send(byte[] data) throws IOException {
        getDos().write(data);
        getDos().flush();
    }

    public void send(MessageProcessor data) throws IOException {
        getDos().write(data.getBytes());
        getDos().flush();
    }

    public String receive() throws IOException {
        String text = getBr().readLine();
        if (null != text)
            LOGGER.info("receive: [" + text + "]");
        return text;
    }

    public byte[] read() throws IOException {
        byte[] data = new byte[MAXLEN];
        int len = getDis().read(data);
        byte[] res = new byte[len];
        System.arraycopy(data, 0, res, 0, len);
        return res;
    }

    public byte[] readUntil(byte[] until) throws IOException {
        byte[] data = new byte[MAXLEN],
                tmp = new byte[until.length];
        byte value;
        int i = 0;
        while ((value = getDis().readByte()) != -1) {
            dequeue(tmp, value);
            data[i++] = value;
            if (Arrays.equals(tmp, until))
                break;
        }
        int size = i + 1 - until.length;
        byte[] res = new byte[size];
        System.arraycopy(data, 0, res, 0, size);
        return res;
    }

    public void close() throws IOException {
        if (bw != null)
            bw.close();
        if (br != null)
            br.close();
        if (dos != null)
            dos.close();
        if (dis != null)
            dis.close();
        socket.close();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
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

    private DataOutputStream getDos() throws IOException {
        if (dos == null)
            this.dos = new DataOutputStream(socket.getOutputStream());
        return dos;
    }

    private DataInputStream getDis() throws IOException {
        if (dis == null)
            this.dis = new DataInputStream(socket.getInputStream());
        return dis;
    }
}
