package komodo.example.rletest.solution;

import org.springframework.web.bind.annotation.*;

@RestController
public class RLEController {

    private RLEEncoder encoder;
    private RLEDecoder decoder;

    public RLEController(final RLEEncoder encoder, final RLEDecoder decoder) {
        this.encoder=encoder;
        this.decoder=decoder;
    }

    @RequestMapping("/")
    public String helloWorld() {
        return "Hello, World";
    }

    @PutMapping("/encode")
    public EncodeDecodeResponse encode(@RequestBody final EncodeDecodeRequest request) {
        long start = System.nanoTime();
        final String responseText =encoder.encode(request.getText());
        long duration = System.nanoTime()-start;
        return new EncodeDecodeResponse(duration, responseText);
    }

    @PutMapping("/decode")
    public EncodeDecodeResponse decode(@RequestBody final EncodeDecodeRequest request) {
        long start = System.nanoTime();
        final String responseText =decoder.decode(request.getText());
        long duration = System.nanoTime()-start;
        return new EncodeDecodeResponse(duration, responseText);
    }
}
