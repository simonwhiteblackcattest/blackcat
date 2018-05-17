package komodo.example.rletest.solution;

import org.springframework.stereotype.Component;

@Component
public class RLEProperties {

    private Character escape='#';

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
