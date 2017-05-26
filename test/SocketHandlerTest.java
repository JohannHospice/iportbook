import com.iportbook.core.tools.net.SocketHandler;
import junit.framework.TestCase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandlerTest extends TestCase {
    private ServerSocket server;
    private final static int PORT = 9999;

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

    public void testSend() throws IOException {
        SocketHandler socketHandler = new SocketHandler("localhost", PORT);

        Socket so = server.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));

        String msgSend = "Salut";
        socketHandler.send(msgSend);

        String msgReceive = br.readLine();

        br.close();
        so.close();
        socketHandler.close();

        assertEquals(msgSend, msgReceive);
    }

    public void testReceive() throws IOException {
        SocketHandler socketHandler = new SocketHandler("localhost", PORT);

        Socket so = server.accept();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(so.getOutputStream()));
        String msgSend = "Salut";

        pw.print(msgSend+'\n');
        pw.flush();

        String msgReceive = socketHandler.receive();

        pw.close();
        so.close();
        socketHandler.close();

        assertEquals(msgSend, msgReceive);
    }
}
