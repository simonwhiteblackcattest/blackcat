package komodo.example.rletest.solution;

import komodo.example.rletest.iface.Encoder;
import org.springframework.stereotype.Component;

/**
 *
 * This encoder only supports strings containing charcaters with code points less than U+FFFF (that is, those that sit
 * into a single UTF-16 word).  If the input string contains characters in supplementary code points, then the resulting
 * encoding will still decode correctly (given a decoder which makes the same assumption) but this encoding will not
 * compress repeated characters in the supplementary range.  If we needed to fully support ALL valid unicode byte
 * sequences, we would use String.codePointAt() to extract the multi-word symbols, Charecter.toChars() and
 * new String(char[]) to get the symbols to compare but this is much slower so I have assumed only handling UTF-16 words
 * is OK.
 *
 * In a real-world setting I would check this assumption with
 * product or requirement owner, but I'm trying to fit this in around normal working hours, so I hope you don't mind!
 *
 * We use a configurable escape character to handle potentially ambigious strings, and double-escape to handle
 * instances of the escape in the input string.
 *
 *
 */

@Component
public class RLEEncoder implements Encoder {

    private Character escape;
    private int threshold;

    public RLEEncoder(final RLEProperties properties) {
        this.escape=properties.getEscape();
        this.threshold=properties.getRepetitionThreshold();
    }

    /**
     * Crawl the input string, remembering the last character we saw and flushing input back out again when that changes.
     * @param original String to encode
     * @return Encoded string
     */
    @Override
    public String encode(final String original) {
        if (original==null || original.length()<threshold) { //Shortcut naive input, and allows simpler initialisaton of the loop below
            return original;
        }

        final StringBuilder out = new StringBuilder(original.length());
        char current = original.charAt(0);
        int count=1;
        for (int idx=1; idx < original.length(); idx++) {
            final char next = original.charAt(idx);
            if (current==next) {
                count++;
            }
            else {
                flush(current, count, out);
                current=next;
                count=1;
            }
        }
        flush(current, count, out);
        return out.toString();
    }

    /**
     * Called when we have encountered a change in character or at the end of the string
     * @param toFlush charecter to flush
     * @param count number of instances of that character we have encountered
     * @param destination StringBuilder to flush to
     */
    private final void flush(final char toFlush, final int count, final StringBuilder destination) {
        final boolean isEscape = toFlush==this.escape || toFlush=='{'; //Escape both { and the escape char itself

        if (count<this.threshold) {
            for (int flushCount = 0; flushCount < count; flushCount++) {
                if (isEscape) {
                    destination.append(this.escape);
                }
                destination.append(toFlush);
            }
        }
        else {
            destination.append('{').append(toFlush).append(';').append(count).append('}');
        }

    }
}
