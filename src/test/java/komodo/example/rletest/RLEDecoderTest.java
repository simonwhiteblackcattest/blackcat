package komodo.example.rletest;

import komodo.example.rletest.iface.Decoder;
import komodo.example.rletest.solution.RLEDecoder;
import komodo.example.rletest.solution.RLEProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RLEDecoderTest {

    private Decoder fixture;

    @Before
    public void init() {
        fixture = new RLEDecoder(new RLEProperties());
    }

    @Test
    public void basicTest() {
        final String input="XYZAAAAABBBB{C;8}DDDD{E;9}FFFF{H;10}PP";
        final String expected="XYZAAAAABBBBCCCCCCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String result = fixture.decode(input);
        assertEquals(expected, result);
    }

    @Test
    public void mlRunicTest() {
        final String input="ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁ{ᚱ;13}ᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗ";
        final String expected="ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗ";
        final String result = fixture.decode(input);
        assertEquals(expected, result);
    }

    @Test
    public void mlChineseTest() {
        final String input="{就;10}可消垻,只有当所有方块都被消垻时才可以过关";
        final String expected="就就就就就就就就就就可消垻,只有当所有方块都被消垻时才可以过关";
        final String result = fixture.decode(input);
        assertEquals(expected, result);
    }

    /* Uses Unicode supplementary charecters (>+FFFF)  - google il8nguy for more resources*/
    @Test
    public void mlShavianTest() {
        final String input="\uD801\uDC61\uD801\uDC79\uD801\uDC61 ·\uD801\uDC5A\uD801\uDC7B\uD801\uDC6F\uD801\uDC78\uD801\uDC5B ·\uD801\uDC56\uD801\uDC77";
        final String result = fixture.decode(input);
        assertEquals(input, result);
    }

    @Test
    public void escapingTest() {
        final String input="XYZAAAAA#{Z;10}BBBBCCCC#{Y;20}CCCCDDDD{E;9}FFFF{H;10}PP";
        final String expected = "XYZAAAAA{Z;10}BBBBCCCC{Y;20}CCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String result = fixture.decode(input);
        assertEquals(expected, result);
    }
}
