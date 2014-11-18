package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Entity
public class Player extends Model {

    @Id
    public Long id;

    @Column(length = 255, unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.MinLength(2)
    @Constraints.Required
    @Constraints.Email
    private String email;

    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.MinLength(2)
    @Constraints.Required
    public String name;

    @Column(length = 64, nullable = false)
    @JsonIgnore
    private byte[] password;

    @Column(nullable = false)
    @Constraints.Required
    @JsonIgnore
    public Date createdDate;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    public List<AuthToken> authTokens;

    /*
        Custom access methods
     */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : null;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = getSha512(password);
    }

    public String createAuthToken() {
        AuthToken token = new AuthToken(this);
        token.save();
        return token.token;
    }

    public void revokeAuthToken(String token) {
        AuthToken.revokeToken(token);
    }

    public void revokeAllAuthTokens() {
        AuthToken.revokeAllTokens(this);
    }

    /*
        Creation and retrieval
     */

    public Player(String email, String name, String password) {
        setEmail(email);
        this.name = name;
        setPassword(password);
        this.createdDate = new Date();
    }

    public static final Finder<Long, Player> FINDER = new Finder<Long, Player>(Long.class, Player.class);

    public static Player findById(long id) {
        return FINDER.byId(id);
    }

    public static List<Player> findAll() {
        return FINDER.orderBy("id").findList();
    }

    public static Player findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        return FINDER.where()
                .contains("authTokens.token", authToken)
                .findUnique();
    }

    public static Player findByEmailAndPassword(String emailAddress, String password) {
        if (emailAddress == null || password == null) {
            return null;
        }

        return FINDER.where()
                .eq("emailAddress", emailAddress.toLowerCase())
                .eq("password", getSha512(password))
                .findUnique();
    }

    /*
        Utility methods
     */

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
