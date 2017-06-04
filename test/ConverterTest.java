import com.iportbook.core.tools.Converter;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;

public class ConverterTest extends TestCase {
    private byte[][] values = {
            new byte[]{(byte) 255, (byte) 255, 0, 0},
            new byte[]{(byte) 0, (byte) 0, 0, 0},
            new byte[]{(byte) 0, (byte) 1, 0, 0},
            new byte[]{(byte) 8, (byte) 23, 0, 0},
            new byte[]{(byte) 10, (byte) 0, 0, 0},
    };
    private int[] keys = {65535, 0, 256, 5896, 10};

    public void testByteArrayToInt() throws Exception {
        for (int i = 0; i < keys.length; i++)
            Assert.assertEquals(keys[i], Converter.littleEndianToInt(values[i]));
    }

    public void testIntToByteArray() throws Exception {
        for (int i = 0; i < keys.length; i++)
            Assert.assertEquals(Arrays.toString(values[i]), Arrays.toString(Converter.intToByteArray(keys[i])));
    }
}