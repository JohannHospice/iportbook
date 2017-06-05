package com.iportbook.core.tools.net;

import com.iportbook.core.tools.Utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class DataSocket {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final int SIZE_MESSAGE_MAX = 300;
    private final Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public DataSocket(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public DataSocket(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void bind(String host, int port) throws IOException {
        this.socket.bind(new InetSocketAddress(host, port));
    }

    public void send(byte[] data) throws IOException {
        getDos().write(data);
        getDos().flush();
        Logger.getGlobal().info("send: [" + Arrays.toString(data) + "]");
    }

    public void send(byte[] data, int offset, int length) throws IOException {
        getDos().write(data, offset, length);
        getDos().flush();
        Logger.getGlobal().info("send: [" + Arrays.toString(data) + "]");
    }

    public byte[] read() throws IOException {
        byte[] data = new byte[SIZE_MESSAGE_MAX];
        int len = getDis().read(data);
        if (len == -1)
            return null;
        byte[] res = new byte[len];
        System.arraycopy(data, 0, res, 0, len);
        Logger.getGlobal().info("receive: [" + Arrays.toString(res) + "]");
        return res;
    }

    /**
     * Write until some bytes, thoses bytes are not in the return result
     *
     * @param until byte[]
     * @return byte[]
     * @throws IOException
     */
    public byte[] readUntil(byte[] until) throws IOException {
        byte[] data = new byte[SIZE_MESSAGE_MAX],
                tmp = new byte[until.length];
        byte value;
        int i = 0;
        while ((value = getDis().readByte()) != -1) {
            Utility.dequeue(tmp, value);
            data[i++] = value;
            if (Arrays.equals(tmp, until))
                break;
        }
        int size = i + 1 - until.length;
        byte[] res = new byte[size];
        System.arraycopy(data, 0, res, 0, size);
        Logger.getGlobal().info("receive: [" + Arrays.toString(res) + "]");
        return res;
    }

    public void close() throws IOException {
        if (dos != null)
            dos.close();
        if (dis != null)
            dis.close();
        socket.close();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
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
