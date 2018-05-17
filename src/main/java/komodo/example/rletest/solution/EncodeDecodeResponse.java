package komodo.example.rletest.solution;

public class EncodeDecodeResponse {

    private long time;
    private String text;

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public EncodeDecodeResponse(long time, String text) {

        this.time = time;
        this.text = text;
    }
}
