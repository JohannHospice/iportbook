import com.iportbook.core.tools.net.DatagramSocketHandler;
import junit.framework.TestCase;

public class DatagramSocketTest extends TestCase {
    private final static int PORT = 5555;

    private DatagramSocketHandler ddaSo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ddaSo = new DatagramSocketHandler(PORT, PORT);
    }

    public void testSendReceive() throws Exception {
        String send = "Salut";
        ddaSo.send(send);
        String receive = ddaSo.receive();
        assertEquals(send, receive);
    }
}
