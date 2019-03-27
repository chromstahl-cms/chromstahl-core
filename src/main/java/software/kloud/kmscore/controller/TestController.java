package software.kloud.kmscore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test/403")
    public ResponseEntity<String> get403() {
        return ResponseEntity.status(403).build();
    }
}
