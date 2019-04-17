package software.kloud.kmscore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.persistence.security.entities.RoleJpaRecordAdapter;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials()
                .toString();

        var user = userRepository.findByUserName(userName).orElseThrow(() -> new SecurityException("No user found"));

        var roles = user.getRoleJpaRecords()
                .stream()
                .map(RoleJpaRecordAdapter::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken
                (userName, password, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
