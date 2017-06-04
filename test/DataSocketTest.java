import com.iportbook.core.tools.net.DataSocket;
import junit.framework.TestCase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class DataSocketTest extends TestCase {
    private ServerSocket server;
    private final static int PORT = 8956;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        server = new ServerSocket(PORT);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        server.close();
    }

    private byte[] reduce(byte[] data, int offset, int length) {
        byte[] res = new byte[length - offset];
        System.arraycopy(data, offset, res, 0, length);
        return res;
    }

    public void testSendString() throws IOException {
        DataSocket dataSocket = new DataSocket("localhost", PORT);

        Socket so = server.accept();
        DataInputStream br = new DataInputStream(so.getInputStream());

        byte[] msgSend = "Salut".getBytes();
        dataSocket.send(msgSend);

        byte[] msgReceive = new byte[10];
        int len = br.read(msgReceive);

        br.close();
        so.close();
        dataSocket.close();

        assertEquals(Arrays.toString(msgSend), Arrays.toString(reduce(msgReceive, 0, len)));
    }

    public void testReceiveString() throws IOException {
        DataSocket dataSocket = new DataSocket("localhost", PORT);

        Socket so = server.accept();
        DataOutputStream pw = new DataOutputStream(so.getOutputStream());
        byte[] msgSend = "Salut".getBytes();

        pw.write(msgSend);
        pw.flush();

        byte[] msgReceive = dataSocket.read();
        pw.close();
        so.close();
        dataSocket.close();

        assertEquals(Arrays.toString(msgSend), Arrays.toString(msgReceive));
    }

    public void testSendBytes() throws IOException {
        DataSocket dataSocket = new DataSocket("localhost", PORT);

        Socket so = server.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));

        String msgSend = "Salut";
        dataSocket.send(msgSend.getBytes());
        char[] d = new char[100];
        int len = br.read(d);

        br.close();
        so.close();
        dataSocket.close();

        assertEquals(msgSend.length(), len);
        assertEquals(msgSend, new String(d, 0, len));
    }

    public void testReceiveBytes() throws IOException {
        DataSocket dataSocket = new DataSocket("localhost", PORT);

        Socket so = server.accept();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(so.getOutputStream()));
        String msgSend = "Salut";

        pw.print(msgSend);
        pw.flush();

        byte[] msgReceive = dataSocket.read();
        assertEquals(msgSend, new String(msgReceive, 0, msgReceive.length));

        pw.close();
        so.close();
        dataSocket.close();
    }
}
