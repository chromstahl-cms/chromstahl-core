package software.kloud.kmscore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import software.kloud.kmscore.persistence.repositories.UserRepository;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials()
                .toString();

//        Advertisers advertisers = advertiserRepository
//                .findByEmailAddress(email)
//                .orElseThrow(() -> new UsernameNotFoundException("UserJpaRecord not found"));
//
//        if (!BCrypt.checkpw(password, advertisers.getPassword())) {
//            throw new BadCredentialsException("Bad credentials");
//        }

        return new UsernamePasswordAuthenticationToken
                (email, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
