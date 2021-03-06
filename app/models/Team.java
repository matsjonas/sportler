package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
public class Team extends Model {

    private static final Logger.ALogger LOGGER = Logger.of(Team.class);

    @Id
    public long id;

    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.MinLength(2)
    @Constraints.Required
    public String name;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public Player owner;

    @Column(nullable = false)
    @Constraints.Required
    @JsonIgnore
    public Date createdDate;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "team_admins")
    @Constraints.Required
    @JsonIgnore
    public List<Player> admins;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "team_members")
    @JsonIgnore
    public List<Player> members;

    /*
        Custom access methods
     */

    public boolean addAdmin(Player player) {
        if (admins.contains(player)) {
            LOGGER.info(String.format("Not adding player %d to team %d admins. Already present.", player.id, this.id));
            return false;
        } else {
            return admins.add(player);
        }
    }

    public boolean removeAdmin(Player player) {
        if (admins.contains(player)) {
            return admins.remove(player);
        } else {
            LOGGER.info(String.format("Not removing player %d from team %d admins. Already absent.", player.id, this.id));
            return false;
        }
    }

    public boolean addMember(Player player) {
        if (members.contains(player)) {
            LOGGER.info(String.format("Not adding player %d to team %d members. Already present.", player.id, this.id));
            return false;
        } else {
            return members.add(player);
        }
    }

    public boolean removeMember(Player player) {
        if (members.contains(player)) {
            return members.remove(player);
        } else {
            LOGGER.info(String.format("Not removing player %d from team %d members. Already absent.", player.id, this.id));
            return false;
        }
    }

    /*
        Creation and retrieval
     */

    public Team(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.createdDate = new Date();
        this.admins = Arrays.asList(owner);
        this.members = Arrays.asList(owner);
    }

    public static Finder<Long, Team> FINDER = new Finder<Long, Team>(Long.class, Team.class);

    public static Team findById(Long id) {
        return FINDER.byId(id);
    }

    public static List<Team> findAll() {
        return FINDER.orderBy("id").findList();
    }

}
