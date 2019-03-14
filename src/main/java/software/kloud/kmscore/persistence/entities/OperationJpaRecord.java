package software.kloud.kmscore.persistence.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OperationJpaRecord implements GrantedAuthority {
    @Id
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}
