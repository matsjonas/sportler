package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

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
    public Date createdDate;

    @ManyToMany
    @JoinTable(name = "team_admins")
    @Constraints.Required
    public List<Player> admins;

    @ManyToMany
    @JoinTable(name = "team_members")
    public List<Player> members;

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
