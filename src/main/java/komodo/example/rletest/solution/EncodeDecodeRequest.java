package komodo.example.rletest.solution;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncodeDecodeRequest {

    @JsonProperty
    private String text;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
