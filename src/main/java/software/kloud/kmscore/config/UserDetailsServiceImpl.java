package software.kloud.kmscore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.persistence.security.entities.RoleJpaRecordAdapter;

import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .map(user -> new User(user.getUserName(), user.getPassword(), user.getRoleJpaRecords()
                        .stream()
                        .map(RoleJpaRecordAdapter::new)
                        .collect(Collectors.toList())))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}


