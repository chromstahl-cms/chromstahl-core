package software.kloud.kmscore.persistence.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RoleJpaRecord implements GrantedAuthority {

    @OneToMany
    private final List<OperationJpaRecord> allowedOperationJpaRecords = new ArrayList<>();
    @Id
    private String id;
    @OneToMany
    private List<UserJpaRecord> userJpaRecordList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserJpaRecord> getUserJpaRecordList() {
        return userJpaRecordList;
    }

    public void setUserJpaRecordList(List<UserJpaRecord> userJpaRecordList) {
        this.userJpaRecordList = userJpaRecordList;
    }

    public List<OperationJpaRecord> getAllowedOperations() {
        return allowedOperationJpaRecords;
    }

    @Override
    public String getAuthority() {
        return id;
    }
}
