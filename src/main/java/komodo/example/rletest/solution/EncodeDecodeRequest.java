package komodo.example.rletest.solution;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * We use the same class for encoding and decoding requests - a more advanced solution might use the spring
 * validator to handle decode requests differently but in this implementation we assume that any valid string
 * is a potentially valid encode/decode request
 */
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
