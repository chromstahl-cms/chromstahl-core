package de.fredlahde;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import software.kloud.KMSPluginSDK.KMSStorage;

@Controller
public class TestController {

    private final KMSStorage storage;

    public TestController(KMSStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/foo")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(storage.getRoot().getAbsolutePath());
    }
}
