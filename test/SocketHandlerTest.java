import com.iportbook.app.tcp.SocketHandler;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * Created by djihe on 13/05/2017.
 */
public class SocketHandlerTest extends TestCase {
    SocketHandler socketHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        socketHandler = new SocketHandler("localhost", 6666);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        socketHandler.close();
    }

    public void testSendReceive() throws IOException {
        String send = "Salut";
        socketHandler.send(send);
        String receive = socketHandler.receive();
        assertEquals(send, receive);
    }
}
