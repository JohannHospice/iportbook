import com.iportbook.core.tools.message.MessageUDP;
import junit.framework.TestCase;

import java.util.Arrays;

public class MessageUDPTest extends TestCase {
    public void testParse() throws Exception {
        MessageUDP actual = new MessageUDP(new byte[]{0, 1, 0});
        MessageUDP expected = new MessageUDP(0, 1);

        assertEquals(expected.getFluxSize(), actual.getFluxSize());
        assertEquals(expected.getNotificationCode(), actual.getNotificationCode());
    }

    public void testCompose() throws Exception {
        byte[] actual, expected;

        actual = new MessageUDP(0, 1).getBytes();
        expected = new byte[]{0, 1, 0};
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));

        actual = new MessageUDP(2, 1).getBytes();
        expected = new byte[]{2, 1, 0};
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }
}
