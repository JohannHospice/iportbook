import com.iportbook.core.modele.Client;
import com.iportbook.core.modele.Flux;
import com.iportbook.core.tools.io.Serializer;
import com.iportbook.core.tools.processor.MessageProcessor;
import junit.framework.TestCase;

import java.util.ArrayList;

public class SerializerTest extends TestCase {
    private ArrayList<Client> data = new ArrayList<>();
    private String filename = "test.bin";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Client cli1 = new Client("choco", 45, 9879);
        Client cli = new Client("choco", 45, 9879);
        cli1.addFriendsId(cli);
        cli.addFriendsId(cli1);
        cli.addFlux(3, new MessageProcessor("FRIEN>").set("fds").getBytes());
        Flux fl = new Flux(3, new MessageProcessor("FRIEN>").setId("elds").setId("jklj").getBytes());
        cli.addFluxNotify(fl);
        data.add(cli1);
        data.add(cli);
    }

    public void testWrite() throws Exception {
        Serializer.write(filename, data);
    }

    public void testRead() throws Exception {
        ArrayList<Client> restore = (ArrayList<Client>) Serializer.read(filename);
        assertEquals(data.get(0).getId(), restore.get(0).getId());
    }
}
