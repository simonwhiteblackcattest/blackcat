package komodo.example.rletest;

import komodo.example.rletest.iface.Encoder;
import komodo.example.rletest.solution.RLEEncoder;
import komodo.example.rletest.solution.RLEProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Throughout these tests, I've not made use of parameterised tests, or a mocking framework - in a production system
 * I'd usually see both - the lack of the former is just an artefact of time constraints, and the latter because it's
 * not really required here (one reason I generally prefer constructor injection).  In general though, I've used EasyMock,
 * PowerMock, Mokito and just written my own mocks - of the three, I generally prefer Mokito.
 */
public class RLEEncoderTest {

    private Encoder fixture;

    @Before
    public void init() {
        fixture = new RLEEncoder(new RLEProperties());
    }

    @Test
    public void basicTest() {
        final String input = "XYZAAAAABBBBCCCCCCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String expected="XYZAAAAABBBB{C;8}DDDD{E;9}FFFF{H;10}PP";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    /**
     * Includes instances of {A;10} in the input string
     */
    @Test
    public void escapingTest() {
        final String input = "XYZAAAAA{Z;10}BBBBCCCC{Y;20}CCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String expected="XYZAAAAA#{Z;10}BBBBCCCC#{Y;20}CCCCDDDD{E;9}FFFF{H;10}PP";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void inputContainsEscapeCharcactersTest() {
        final String input = "XYZAAAAA{Z;10}BBBB#CCCC#{Y;20}##CCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP";
        final String expected="XYZAAAAA#{Z;10}BBBB##CCCC###{Y;20}####CCCCDDDD{E;9}FFFF{H;10}PP";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    /**
     * We start what looks like something tht requires escaping, but the string ends before our pattern completes.
     * Do we drop the incomplete pattern (no)?  Do we remember to escape it? (yes)
     */
    @Test
    public void escapeOffEndOfStringTest() {
        final String input = "XYZAAAAABBBBCCCCCCCCDDDDEEEEEEEEEFFFFHHHHHHHHHHPP{A;1";
        final String expected="XYZAAAAABBBB{C;8}DDDD{E;9}FFFF{H;10}PP#{A;1";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    /**
     * This is as much a test of your IDE as it is the software :-) - should be old english runes
     */
    @Test
    public void mlRunicTest() {
        final String input="ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚱᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗ";
        final String expected="ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁ{ᚱ;13}ᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗ";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void mlChineseTest() {
        final String input="就就就就就就就就就就可消垻,只有当所有方块都被消垻时才可以过关";
        final String expected="{就;10}可消垻,只有当所有方块都被消垻时才可以过关";
        final String result = fixture.encode(input);
        assertEquals(expected, result);
    }

    /**
     * Uses Unicode supplementary charecters (>+FFFF)  - google il8nguy for more resources.
     * As discussed in the class comments of RLEEncoder, this won't actually compress in this impleentation
     * so our goal instead is to ensure the string is not corrupted (and hence, can still be decoded)
     */
    @Test
    public void mlShavianTest() {
        final String input="\uD801\uDC61\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC79\uD801\uDC61 ·\uD801\uDC5A\uD801\uDC7B\uD801\uDC6F\uD801\uDC78\uD801\uDC5B ·\uD801\uDC56\uD801\uDC77";
        final String result = fixture.encode(input);
        assertEquals(input, result);
    }

}