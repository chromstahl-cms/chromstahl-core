package software.kloud.kmscore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.kloud.kms.entities.UserJpaRecord;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.dto.RegisterDTO;
import software.kloud.kmscore.dto.RegisterResponseDTO;
import software.kloud.kmscore.util.TokenFactory;

@Controller
public class RegisterController extends AbsController {
    private final TokenFactory tokenFactory;
    private final UserRepository userRepository;

    public RegisterController(TokenFactory tokenFactory, UserRepository userRepository) {
        this.tokenFactory = tokenFactory;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> store(@RequestBody RegisterDTO registerDTO) {
        var respDTO = new RegisterResponseDTO();

        if (userRepository.findByUserName(registerDTO.getUserName()).isPresent()) {
            respDTO.addRequestError("userName", "userName already taken");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(respDTO);
        }
        var user = new UserJpaRecord();

        user.setUserName(registerDTO.getUserName());
        user.setEmail(registerDTO.geteMail());
        user.setPassword(registerDTO.getPassword());

        if (checkForViolations(respDTO, user)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(respDTO);
        }

        var token = tokenFactory.generateToken(user);
        respDTO.setToken(token.getToken());
        return ResponseEntity.ok(respDTO);
    }
}
