import com.iportbook.core.modele.Client;
import com.iportbook.core.tools.net.SocketHandler;
import com.iportbook.core.tools.message.MessageTCP;
import com.iportbook.app.server.client.ServerClient;
import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

public class ServerClientTest extends TestCase {

    private ServerClient serverClient;
    private final static int PORTSERVER = 8888;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serverClient = new ServerClient(PORTSERVER);
        serverClient.getClientManager().addClient(new Client("bachata", "bachata", 8885));
        new Thread(serverClient).start();

        TimeUnit.SECONDS.sleep(2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        serverClient.stop();
    }

    public void testRegister() throws Exception {
        String id = "jojolescroc", mdp = "password", port = "5555";

        SocketHandler so = new SocketHandler("localhost", PORTSERVER);

        so.sendMessage(new MessageTCP(MessageTCP.Type.REGIS)
                .addArgument(id)
                .addArgument(port)
                .addArgument(mdp));
        MessageTCP.Type typeCo = so.receiveMessage().getType();

        so.sendMessage(new MessageTCP(MessageTCP.Type.IQUIT));
        MessageTCP.Type typeQu = so.receiveMessage().getType();
        so.close();

        Client client = serverClient.getClientManager().getClient(id);


        assertEquals(MessageTCP.Type.WELCO, typeCo);
        assertEquals(MessageTCP.Type.GOBYE, typeQu);
        assertEquals(id, client.getId());
        assertEquals(Integer.parseInt(port), client.getPortUDP());
        assertEquals(mdp, client.getPassword());
    }

    public void testLogin() throws Exception {
        SocketHandler so = new SocketHandler("localhost", PORTSERVER);
        so.sendMessage(new MessageTCP(MessageTCP.Type.CONNE)
                .addArgument("bachata")
                .addArgument("bachata"));
        MessageTCP.Type typeCo = so.receiveMessage().getType();
        so.sendMessage(new MessageTCP(MessageTCP.Type.IQUIT));
        MessageTCP.Type typeQu = so.receiveMessage().getType();
        so.close();

        assertEquals(MessageTCP.Type.HELLO, typeCo);
        assertEquals(MessageTCP.Type.GOBYE, typeQu);
    }
}
