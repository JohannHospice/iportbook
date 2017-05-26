import com.iportbook.core.tools.io.Serializer;
import junit.framework.TestCase;

import java.util.ArrayList;

public class SerializerTest extends TestCase {
    private ArrayList<String> data = new ArrayList<>();
    private String filename = "data.txt";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        data.add("jnkhjk");
        data.add("jnkbvchjk");
        data.add("gfd");
        data.add("jnktyryhjk");
        data.add("jnkfdsrehjk");
        data.add("jbvcnkhjk");
    }

    public void testWrite() throws Exception {
        Serializer.write(filename, data);
    }

    public void testRead() throws Exception {
        ArrayList<String> restore = (ArrayList<String>) Serializer.read(filename);
        assertEquals(data, restore);
    }
}
