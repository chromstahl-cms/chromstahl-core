package software.kloud.kmscore.persistence.security.entities;

import org.springframework.security.core.GrantedAuthority;
import software.kloud.kms.entities.OperationJpaRecord;
import software.kloud.kms.entities.RoleJpaRecord;

import java.util.List;
import java.util.stream.Collectors;

public class RoleJpaRecordAdapter extends RoleJpaRecord implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return super.getId();
    }

    @Override
    public List<OperationJpaRecord> getAllowedOperations() {
        return super.getAllowedOperations().stream().map(it -> (OperationJpaRecordAdapter) it).collect(Collectors.toList());
    }
}
