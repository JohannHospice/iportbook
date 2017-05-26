import com.iportbook.core.tools.net.DatagramSocketHandler;
import junit.framework.TestCase;

public class DatagramSocketTest extends TestCase {
    private final static int PORT = 5555;

    private DatagramSocketHandler dsoHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dsoHandler = new DatagramSocketHandler(PORT, PORT);
    }

    public void testSendReceive() throws Exception {
        String send = "Salut";
        dsoHandler.send(send);
        String receive = dsoHandler.receive();
        assertEquals(send, receive);
    }
}
