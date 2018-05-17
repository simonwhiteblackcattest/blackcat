package komodo.example.rletest;

import komodo.example.rletest.iface.Decoder;
import komodo.example.rletest.iface.Encoder;
import komodo.example.rletest.solution.RLEDecoder;
import komodo.example.rletest.solution.RLEEncoder;
import komodo.example.rletest.solution.RLEProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * In a production system, I would often expect to see these integration tests in their own package or even their own
 * source tree.  I've put them in here with the unit tests even though they are not strictly unit tests because it's
 * the easiest approach to manage in a relatively small service
 */
public class RLEIntegrationTest {

    private Encoder encoder;
    private Decoder decoder;

    @Before
    public void init() {
        this.encoder=new RLEEncoder(new RLEProperties());
        this.decoder=new RLEDecoder(new RLEProperties());
    }

    @Test
    public void doubleEncodeDoubleDecodeWithEscapesTest() {
        final String input="XYZAAAAA{Z;10}BBBB#CCCC#{Y;20}##CCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String result = decoder.decode(decoder.decode(encoder.encode(encoder.encode(input))));
        assertEquals(input, result);
    }

    @Test
    public void tripleEncodeDecodeWithNonAsciiTest() {
        final String input="就就就就就就就就就就可消垻,只有当所有方块都被消垻时才可以过关";
        final String result = decoder.decode(decoder.decode(decoder.decode(encoder.encode(encoder.encode(encoder.encode(input))))));
        assertEquals(input, result);

    }

}
