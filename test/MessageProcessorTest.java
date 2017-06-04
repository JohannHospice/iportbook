import com.iportbook.core.tools.processor.MessageProcessor;
import junit.framework.TestCase;

import java.util.Arrays;

public class MessageProcessorTest extends TestCase {

    private final String[] tests = {
            "REGIS bob12345 0879 " + (byte) 10 + (byte) 10 + "+++",
            "FRIE? alice123+++",
            "EIRF> bob12345+++",
            "CONSU+++",
    };

    byte[] concat(byte[] a1, byte[] a2) {
        byte[] a3 = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, a3, 0, a1.length);
        System.arraycopy(a2, 0, a3, a1.length, a2.length);
        return a3;
    }

    public void testParse() throws Exception {
        ;

        MessageProcessor message;

        message = new MessageProcessor(tests[1]);
        assertEquals("FRIE?", message.getType());
        assertEquals("alice123", message.getId());

        message = new MessageProcessor(tests[3]);
        assertEquals("CONSU", message.getType());

        message = new MessageProcessor(concat("REGIS bob12345 0879 ".getBytes(), concat(new byte[]{10, 10}, "+++".getBytes())));
        assertEquals("REGIS", message.getType());
        assertEquals("bob12345", message.getId());
        assertEquals(879, message.getPort());
        assertEquals(2570, message.getPassword());
    }

    public void testCompose() throws Exception {
        assertEquals(
                Arrays.toString("FRIE< f4ds56f+++".getBytes()),
                Arrays.toString(new MessageProcessor("FRIE<").setId("f4ds56f").build()));

        assertEquals(
                Arrays.toString("CONSU+++".getBytes()),
                Arrays.toString(new MessageProcessor("CONSU").build()));

        byte[] test = concat("CONNE f4ds56f ".getBytes(), concat(new byte[]{10, 2}, "+++".getBytes()));
        assertEquals(
                Arrays.toString(test),
                Arrays.toString(new MessageProcessor("CONNE").setId("f4ds56f").setPassword(522).build()));
    }
}
