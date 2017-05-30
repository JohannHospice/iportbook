import com.iportbook.core.tools.message.MessageTCP;
import junit.framework.TestCase;

public class MessageTCPTest extends TestCase {

    private final String[] tests = {
            "REGIS bob12345 07878 1010+++",
            "FRIE? alice123+++",
            "EIRF> bob12345+++",
            "CONSU+++",
    };

    public void testParse() throws Exception {
        MessageTCP message;

        message = MessageTCP.parse(tests[0]);
        assertEquals(MessageTCP.Type.REGIS, message.getType());
        assertEquals(MessageTCP.Operator.NONE, message.getOperator());
        assertEquals("bob12345", message.getArgument(0));
        assertEquals("07878", message.getArgument(1));
        assertEquals("1010", message.getArgument(2));

        message = MessageTCP.parse(tests[1]);
        assertEquals(MessageTCP.Type.FRIE, message.getType());
        assertEquals(MessageTCP.Operator.ASK, message.getOperator());
        assertEquals("alice123", message.getArgument(0));

        message = MessageTCP.parse(tests[3]);
        assertEquals(MessageTCP.Type.CONSU, message.getType());
        assertEquals(MessageTCP.Operator.NONE, message.getOperator());
        assertEquals(0, message.getArgumentSize());
    }

    public void testCompose() throws Exception {
        MessageTCP message = new MessageTCP(MessageTCP.Type.CONNE, MessageTCP.Operator.NONE, new String[]{"12", "f4ds56f"});
        assertEquals("CONNE 12 f4ds56f+++", message.compose());

        message = new MessageTCP(MessageTCP.Type.FRIE, MessageTCP.Operator.CLEFT).addArgument("12");
        assertEquals("FRIE< 12+++", message.compose());

        message = new MessageTCP(MessageTCP.Type.CONSU, MessageTCP.Operator.NONE);
        assertEquals("CONSU+++", message.compose());
    }
}
