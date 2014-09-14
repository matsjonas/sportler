package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
public class Season extends Model {

    @Id
    public long id;

    @Constraints.Required
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.MinLength(2)
    public String name;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public Player owner;

    @Constraints.Required
    public boolean open;

    @Column(nullable = false)
    @Constraints.Required
    public Date createdDate;

    @ManyToMany
    @JoinTable(name = "season_admins")
    @Constraints.Required
    public List<Player> admins;

    @OneToMany(mappedBy = "season")
    public List<Game> games;

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

     /*
        Creation and retrieval
     */

    public Season(String name, Player admin) {
        this.name = name;
        this.admins = Arrays.asList(admin);
        this.open = true;
    }

    public static final Finder<Long, Season> FINDER = new Finder<Long, Season>(Long.class, Season.class);

    public static Season findById(long id) {
        return FINDER.byId(id);
    }

    public static List<Season> findAll() {
        return FINDER.orderBy("id").findList();
    }

}