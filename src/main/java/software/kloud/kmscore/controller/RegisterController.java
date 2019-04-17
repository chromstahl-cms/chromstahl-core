package software.kloud.kmscore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.kloud.kms.entities.UserJpaRecord;
import software.kloud.kms.repositories.RoleRepository;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.dto.RegisterDTO;
import software.kloud.kmscore.dto.TokenResponseDTO;
import software.kloud.kmscore.util.TokenFactory;

import java.util.ArrayList;

@Controller
public class RegisterController extends AbsController {
    private final TokenFactory tokenFactory;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RegisterController(TokenFactory tokenFactory, UserRepository userRepository, RoleRepository roleRepository) {
        this.tokenFactory = tokenFactory;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> store(@RequestBody RegisterDTO registerDTO) {
        var respDTO = new TokenResponseDTO();

        if (userRepository.findByUserName(registerDTO.getUserName()).isPresent()) {
            respDTO.addRequestError("userName", "userName already taken");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(respDTO);
        }
        var user = new UserJpaRecord();

        var roles = user.getRoleJpaRecords();

        if (roles == null) {
            roles = new ArrayList<>();
        }

        var adminRole = roleRepository.findById("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("Could not find admin role, check database"));

        roles.add(adminRole);
        user.setRoleJpaRecords(roles);

        user.setUserName(registerDTO.getUserName());
        user.setEmail(registerDTO.geteMail());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));

        if (checkForViolations(respDTO, user)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(respDTO);
        }

        var token = tokenFactory.generateToken(user);
        respDTO.setToken(token.getToken());
        return ResponseEntity.ok(respDTO);
    }
}
