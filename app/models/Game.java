package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Entity
public class Game extends Model {

    @Id
    public long id;

    @Column(nullable = false)
    @ManyToOne
    @Constraints.Required
    public Season season;

    @Column(nullable = false)
    @Constraints.Required
    public int internalOrder;

    @ManyToOne
    public Player homePlayer;

    @ManyToOne
    public Player awayPlayer;

    @ManyToOne
    public Team homeTeam;

    @ManyToOne
    public Team awayTeam;

    @Column(nullable = false)
    @Constraints.Required
    public int homePoints;

    @Column(nullable = false)
    @Constraints.Required
    public int awayPoints;

    @Constraints.Required
    public Date playedDate;

    @Column(nullable = false)
    @Constraints.Required
    public Date createdDate;

    /*
        Creation and retrieval
     */

    public Game(Season season, Player homePlayer, Player awayPlayer, int homePoints, int awayPoints, Date playedDate) {
        this.season = season;
        this.homePlayer = homePlayer;
        this.awayPlayer = awayPlayer;
        this.homePoints = homePoints;
        this.awayPoints = awayPoints;
        this.playedDate = playedDate;
        this.createdDate = new Date();
    }

    public Game(Season season, Team homeTeam, Team awayTeam, int homePoints, int awayPoints, Date playedDate) {
        this.season = season;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homePoints = homePoints;
        this.awayPoints = awayPoints;
        this.playedDate = playedDate;
        this.createdDate = new Date();
    }

    public static Finder<Long, Game> FINDER = new Finder<Long, Game>(Long.class, Game.class);

    public static Game findById(Long id) {
        return FINDER.byId(id);
    }

    public static List<Game> findAllBySeason(Long seasonId) {
        return FINDER.where()
                .eq("season.id", seasonId)
                .orderBy("id")
                .findList();
    }

}
