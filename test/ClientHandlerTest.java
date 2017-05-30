import com.iportbook.app.server.client.ServerClient;
import junit.framework.TestCase;

public class ClientHandlerTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ServerClient sc = new ServerClient(8888);
    }

    public void testSendReceive() throws Exception {

//        new Thread(new ClientHandler(, new Socket())).start();
    }
}
