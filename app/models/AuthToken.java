package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class AuthToken extends Model {

    @Id
    public long id;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public Player player;

    @Constraints.Required
    @Column(length = 36, nullable = false)
    @Constraints.MaxLength(36)
    @Constraints.MinLength(36)
    public String token;

    @Column(nullable = false)
    @Constraints.Required
    @JsonIgnore
    public Date createdDate;

    @Column(nullable = false)
    @Constraints.Required
    @JsonIgnore
    public Date lastUsedDate;

    /*
        Creation and retrieval
     */

    public AuthToken(Player player) {
        this.player = player;
        this.token = UUID.randomUUID().toString();
        this.createdDate = new Date();
        this.lastUsedDate = new Date();
    }

    public static final Finder<Long, AuthToken> FINDER = new Finder<Long, AuthToken>(Long.class, AuthToken.class);

    public static AuthToken findByToken(String token) {
        if (token == null) {
            return null;
        }

        AuthToken authToken = FINDER.where()
                .eq("token", token)
                .findUnique();
        if (authToken != null) {
            authToken.lastUsedDate = new Date();
            authToken.save();
        }
        return authToken;
    }

    public static List<AuthToken> findByPlayer(Player player) {
        if (player == null) {
            return Collections.emptyList();
        }

        return FINDER.where()
                .eq("player", player)
                .findList();
    }

    public static void revokeToken(String token) {
        AuthToken authToken = findByToken(token);
        if (authToken != null) {
            authToken.delete();
        }
    }

    public static void revokeAllTokens(Player player) {
        Ebean.delete(findByPlayer(player));

    }

}
