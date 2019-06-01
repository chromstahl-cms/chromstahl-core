package software.kloud.kmscore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import software.kloud.kms.entities.UserJpaRecord;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.dto.TokenAuthDTO;
import software.kloud.kmscore.dto.TokenResponseDTO;
import software.kloud.kmscore.util.TokenFactory;

@Controller
@RequestMapping("/token")
public class TokenController {
    private final UserRepository userRepository;
    private final TokenFactory tokenFactory;

    @Autowired
    public TokenController(UserRepository userRepository, TokenFactory tokenFactory) {
        this.userRepository = userRepository;
        this.tokenFactory = tokenFactory;
    }

    @PostMapping
    public ResponseEntity<TokenResponseDTO> store(@RequestBody TokenAuthDTO authDTO) {
        UserJpaRecord users = userRepository
                .findByUserName(authDTO.getUserName())
                .orElseThrow(() -> new SecurityException(String.format("User %s not found", authDTO.getUserName())));

        if (BCrypt.checkpw(authDTO.getPassword(), users.getPassword())) {
            String token = this.tokenFactory.generateToken(users).getToken();
            return ResponseEntity.ok(new TokenResponseDTO(token));
        }

        throw new SecurityException("Could not login user. Incorrect password");
    }
}
