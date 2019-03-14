package software.kloud.kmscore.persistence.entities;

import software.kloud.sc.SilverCommunication;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "token")
public class TokenJpaRecord implements SilverCommunication {

    @ManyToOne
    private UserJpaRecord user;

    @Column(length = 512, unique = true)
    private String token;
    private Date expiryDate;
    @Column(name = "silverIdentifier", length = 300)
    private String silverIdentifier;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public UserJpaRecord getUser() {
        return user;
    }

    public void setUser(UserJpaRecord user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getSilverIdentifier() {
        return silverIdentifier;
    }

    @Override
    public void setSilverIdentifier(String s) {
        this.silverIdentifier = s;
    }
}
