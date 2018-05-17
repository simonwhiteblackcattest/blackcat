package komodo.example.rletest.solution;

import org.springframework.stereotype.Component;

/**
 * Holds configurable properties for the RLE Algorithm.  If I had a bit more time, I'd pop in and test the annotations
 * to get these overrideable by a .properties file
 */
@Component
public class RLEProperties {

    /**
     * The escape character to use
     */
    private Character escape='#';

    /**
     * How many consecutive times should we see a character before we replace it with an encoded version?
     */
    private int repetitionThreshold=6;


    public Character getEscape() {
        return escape;
    }

    public void setEscape(Character escape) {
        this.escape = escape;
    }

    public int getRepetitionThreshold() {
        return repetitionThreshold;
    }

    public void setRepetitionThreshold(int repetitionThreshold) {
        this.repetitionThreshold = repetitionThreshold;
    }
}
