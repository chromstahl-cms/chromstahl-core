package software.kloud.kmscore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import software.kloud.kms.repositories.UserRepository;

import java.security.Principal;

@Controller
public class TestController {
    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test/403")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> get403(@AuthenticationPrincipal Principal principal) {
        String userName = (String) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        var user = userRepository.findByUserName(userName).orElseThrow(() -> new SecurityException("No user found"));

        System.out.println(user);
        return ResponseEntity.status(200).build();
    }
}
