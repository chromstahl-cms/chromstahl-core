package software.kloud.kmscore.persistence.security.entities;

import org.springframework.security.core.GrantedAuthority;
import software.kloud.kms.entities.OperationJpaRecord;

import javax.persistence.Entity;
import javax.persistence.Table;

public class OperationJpaRecordAdapter implements GrantedAuthority {
    private final OperationJpaRecord record;

    public OperationJpaRecordAdapter(OperationJpaRecord record) {
        this.record = record;
    }

    @Override
    public String getAuthority() {
        return record.getId();
    }
}
