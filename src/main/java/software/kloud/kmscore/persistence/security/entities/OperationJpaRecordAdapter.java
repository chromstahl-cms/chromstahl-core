package software.kloud.kmscore.persistence.security.entities;

import org.springframework.security.core.GrantedAuthority;
import software.kloud.kms.entities.OperationJpaRecord;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "operation")
public class OperationJpaRecordAdapter extends OperationJpaRecord implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return super.getId();
    }
}
