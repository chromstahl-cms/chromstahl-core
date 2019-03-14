package software.kloud.kmscore.persistence.entities;

import software.kloud.sc.SilverCommunication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserJpaRecord implements SilverCommunication {
    @OneToMany
    private final List<RoleJpaRecord> roleJpaRecords = new ArrayList<>();
    private String userName;
    private String password;
    @OneToMany
    private List<TokenJpaRecord> tokens;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
    private String email;
    @Column(name = "silverIdentifier", length = 300)
    private String silverIdentifier;

    public List<TokenJpaRecord> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenJpaRecord> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String getSilverIdentifier() {
        return silverIdentifier;
    }

    @Override
    public void setSilverIdentifier(String s) {
        silverIdentifier = s;
    }

    public List<RoleJpaRecord> getRoleJpaRecords() {
        return roleJpaRecords;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
