package komodo.example.rletest.solution;

import komodo.example.rletest.iface.Decoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation makes the same assumption as RLEEncoder, but further assumes both that the length of the
 * encoded String is <2GB and that the length of the decoded String is <4GB.  RTEs will be thrown otherwise.  If we
 * wanted to support very long strings, then we would switch to a streaming approach (which is going to be preferable
 * way before we hit the actual limits of what can be stored in a String)
 */
@Component
public class RLEDecoder implements Decoder {

    final Character escape;
    final int threshold;
    final Pattern compressionRe = Pattern.compile("\\{(.);([0-9]+)\\}");

    public RLEDecoder(final RLEProperties properties) {
        this.escape=properties.getEscape();
        this.threshold=properties.getRepetitionThreshold();
    }

    /**
     *
     * @param encoded
     * @return
     * @throws RuntimeException if an invalid sequence of escapes is specified, but in a production environment we'd
     * usually be throwing something more specific, or the interface would alow for a relevant checked exception like
     * ParseException
     */
    @Override
    public String decode(final String encoded)  {

        if (encoded==null || encoded.length()<5) { //5 == "{A;1}".length()
            return encoded;
        }

        final StringBuilder out = new StringBuilder(encoded.length()*2);    // *2 - reasonable balance to avoid
                                                                                    // growing the array but not waste
                                                                                    // too much memory

        int escapeCount=0;
        for (int idx=0; idx < encoded.length(); idx++) {
            char next = encoded.charAt(idx);
            if (next==this.escape) {
                escapeCount++;
            }
            else {
                boolean escaped=false;
                if (escaped && !(next=='{')) {
                    throw new RuntimeException(new ParseException("Encountered an escape {"+this.escape+"} unexpectedly", idx));
                }
                if (escapeCount>0) {
                    escaped=flushEscapes(escapeCount, out);
                    escapeCount=0; //???
                }
                if (next=='{' && !escaped) {
                    idx+=parsePotentialCompression(encoded, idx, out);
                }
                else {
                    out.append(next);
                }
            }
        }
        return out.toString();

    }

    private int parsePotentialCompression(final String encoded, final int offset, final StringBuilder destination) {
        final Matcher matcher = compressionRe.matcher(encoded);
        if (matcher.find(offset)) {
            final String theChar = matcher.group(1);
            final int count = Integer.parseInt(matcher.group(2));
            for (int i=0; i < count; i++) {
                destination.append(theChar);
            }
            return matcher.group(0).length()-1;
        }
        //else
        destination.append('{');
        return 0;
    }

    private final boolean flushEscapes(final int escapeCount, final StringBuilder destination) {
        for (int i=0; i < escapeCount/2; i++) {
            destination.append(this.escape);
        }
        return escapeCount%2==1;
    }
}
