import com.iportbook.app.modele.Client;
import com.iportbook.app.net.tcp.SocketHandler;
import com.iportbook.app.tools.Message;
import com.iportbook.server.ServerClient;
import junit.framework.TestCase;

public class ServerClientTest extends TestCase {

    private ServerClient serverClient;
    private final static int PORT = 8886;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serverClient = new ServerClient(PORT);
        serverClient.addClient(new Client("bachata", "bachata", 8885));
        new Thread(serverClient).start();
    }

    public void testRegister() throws Exception {
        String id = "jojolescroc", mdp = "password", port = "5555";

        SocketHandler so = new SocketHandler("localhost", PORT);
        so.sendMessage(new Message(Message.Type.REGIS)
                .addArgument(id)
                .addArgument(port)
                .addArgument(mdp));
        Message.Type type = so.receiveMessage().getType();

        Client client = serverClient.getClientById(id);

        assertEquals(Message.Type.WELCO, type);
        assertEquals(id, client.getId());
        assertEquals(Integer.parseInt(port), client.getPort());
        assertEquals(mdp, client.getPassword());

        so.close();
    }

    public void testLogin() throws Exception {
        SocketHandler so = new SocketHandler("localhost", PORT);
        so.sendMessage(new Message(Message.Type.CONNE)
                .addArgument("bachata")
                .addArgument("bachata"));
        Message.Type typeCo = so.receiveMessage().getType();
        so.sendMessage(new Message(Message.Type.IQUIT));
        Message.Type typeQu = so.receiveMessage().getType();
        so.close();

        assertEquals(Message.Type.HELLO, typeCo);
        assertEquals(Message.Type.GOBYE, typeQu);
    }
}
