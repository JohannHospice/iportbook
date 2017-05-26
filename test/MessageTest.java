import com.iportbook.core.tools.message.Message;
import junit.framework.TestCase;

public class MessageTest extends TestCase {

    private final String[] tests = {
            "REGIS bob12345 07878 1010+++",
            "FRIE? alice123+++",
            "EIRF> bob12345+++",
            "CONSU+++",
    };

    public void testParse() throws Exception {
        Message message;

        message = Message.parse(tests[0]);
        assertEquals(Message.Type.REGIS, message.getType());
        assertEquals(Message.Operator.NONE, message.getOperator());
        assertEquals("bob12345", message.getArgument(0));
        assertEquals("07878", message.getArgument(1));
        assertEquals("1010", message.getArgument(2));

        message = Message.parse(tests[1]);
        assertEquals(Message.Type.FRIE, message.getType());
        assertEquals(Message.Operator.ASK, message.getOperator());
        assertEquals("alice123", message.getArgument(0));

        message = Message.parse(tests[3]);
        assertEquals(Message.Type.CONSU, message.getType());
        assertEquals(Message.Operator.NONE, message.getOperator());
        assertEquals(0, message.getArgumentSize());
    }

    public void testCompose() throws Exception {
        Message message = new Message(Message.Type.CONNE, Message.Operator.NONE, new String[]{"12", "f4ds56f"});
        assertEquals("CONNE 12 f4ds56f+++", message.compose());

        message = new Message(Message.Type.FRIE, Message.Operator.CLEFT).addArgument("12");
        assertEquals("FRIE< 12+++", message.compose());

        message = new Message(Message.Type.CONSU, Message.Operator.NONE);
        assertEquals("CONSU+++", message.compose());
    }
}
