package software.kloud.kmscore.persistence.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kloud.kmscore.persistence.security.entities.TokenJpaRecord;
import software.kloud.sc.SilverRepository;


import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenJpaRecord, Long>, SilverRepository<TokenJpaRecord> {
    Optional<TokenJpaRecord> findByToken(String token);
}
