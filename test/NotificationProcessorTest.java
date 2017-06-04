import com.iportbook.core.tools.processor.NotificationProcessor;
import junit.framework.TestCase;

import java.util.Arrays;

public class NotificationProcessorTest extends TestCase {
    public void testParse() throws Exception {
        NotificationProcessor actual = new NotificationProcessor(new byte[]{0, 1, 0});
        NotificationProcessor expected = new NotificationProcessor(0, 1);

        assertEquals(expected.getFluxSize(), actual.getFluxSize());
        assertEquals(expected.getCode(), actual.getCode());
    }

    public void testCompose() throws Exception {
        byte[] actual, expected;

        actual = new NotificationProcessor(0, 1).getBytes();
        expected = new byte[]{0, 1, 0};
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));

        actual = new NotificationProcessor(2, 1).getBytes();
        expected = new byte[]{2, 1, 0};
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }
}
