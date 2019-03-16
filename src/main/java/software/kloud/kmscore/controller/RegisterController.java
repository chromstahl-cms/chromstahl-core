package software.kloud.kmscore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.kloud.kmscore.dto.RegisterDTO;
import software.kloud.kmscore.persistence.security.entities.UserJpaRecord;
import software.kloud.kmscore.persistence.security.repositories.UserRepository;
import software.kloud.kmscore.util.TokenFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Controller
public class RegisterController {
    private final static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final UserRepository userRepository;
    private final TokenFactory tokenFactory;

    public RegisterController(UserRepository userRepository, TokenFactory tokenFactory) {
        this.userRepository = userRepository;
        this.tokenFactory = tokenFactory;
    }

    @PostMapping("/register")
    public ResponseEntity<String> store(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.findByUserName(registerDTO.getUserName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Username already taken");
        }
        var user = new UserJpaRecord();

        user.setUserName(registerDTO.getUserName());
        user.setEmail(registerDTO.geteMail());
        user.setPassword(registerDTO.getPassword());

        Set<ConstraintViolation<UserJpaRecord>> violations = factory.getValidator().validate(user);

        if (!violations.isEmpty()) {
            // TODO: Figure out a way to return wrong fields to client
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        // TODO After testing, replace with calls to Silver
        userRepository.save(user);
        var token = tokenFactory.generateToken(user);
        return ResponseEntity.ok(token.getToken());
    }
}
