package software.kloud.kmscore.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kloud.kmscore.persistence.entities.TokenJpaRecord;
import software.kloud.kmscore.persistence.entities.UserJpaRecord;
import software.kloud.sc.SilverRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpaRecord, Long>, SilverRepository<UserJpaRecord> {
    Optional<UserJpaRecord> findByEmail(String email);
    Optional<UserJpaRecord> findByUserName(String username);
}
