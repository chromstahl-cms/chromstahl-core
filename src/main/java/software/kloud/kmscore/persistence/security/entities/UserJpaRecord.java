package software.kloud.kmscore.persistence.security.entities;

import software.kloud.kmscore.util.Constants;
import software.kloud.sc.SilverCommunication;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class UserJpaRecord implements SilverCommunication {
    @OneToMany
    private final List<RoleJpaRecord> roleJpaRecords = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true)
    private Integer id;
    @Size(min = 3)
    @Column(unique = true)
    private String userName;
    @Pattern(regexp = Constants.PASSWORD_REGEX)
    private String password;
    @OneToMany
    private List<TokenJpaRecord> tokens;
    @Pattern(regexp = Constants.EMAIL_REGEX)
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
