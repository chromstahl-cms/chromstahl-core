package software.kloud.kmscore.persistence.security.entities;

import org.springframework.security.core.GrantedAuthority;
import software.kloud.kms.entities.OperationJpaRecord;
import software.kloud.kms.entities.RoleJpaRecord;

import java.util.List;
import java.util.stream.Collectors;

public class RoleJpaRecordAdapter implements GrantedAuthority {
    private final RoleJpaRecord record;

    public RoleJpaRecordAdapter(RoleJpaRecord record) {
        this.record = record;
    }

    @Override
    public String getAuthority() {
        return record.getId();
    }
}
