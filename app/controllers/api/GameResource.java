package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Game;
import models.Player;
import models.Season;
import models.Team;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class GameResource extends Controller {

    public static Result games(long seasonId) {
        List<Game> games;
        if (seasonId == 0) {
            games = Game.findAll();
        } else {
            games = Game.findAllBySeason(seasonId);
        }
        return ok(Json.toJson(games));
    }

    public static Result game(long seasonId, long id) {
        Game game = Game.findById(id);
        if (seasonId != game.season.id) {
            return badRequest();
        }
        return ok(Json.toJson(game));
    }

    public static Result createGame(long seasonId) {
        JsonNode json = request().body().asJson();

        long homePlayerId = 0;
        long awayPlayerId = 0;
        long homeTeamId = 0;
        long awayTeamId = 0;
        try {
            homePlayerId = json.get("homePlayer").get("id").longValue();
            awayPlayerId = json.get("awayPlayer").get("id").longValue();
        } catch (Exception e) {
            // ignored
        }
        try {
            homeTeamId = json.get("homeTeam").get("id").longValue();
            awayTeamId = json.get("awayTeam").get("id").longValue();
        } catch (NullPointerException e) {
            // ignored
        }
        int homePoints = json.get("homePoints").intValue();
        int awayPoints = json.get("awayPoints").intValue();

        Season season = Season.findById(seasonId);

        if (homeTeamId > 0) {
            Team homeTeam = Team.findById(homeTeamId);
            Team awayTeam = Team.findById(awayTeamId);
            Game game = new Game(season, homeTeam, awayTeam, homePoints, awayPoints, null);
            game.save();

            return ok(Json.toJson(game));
        } else {
            Player homePlayer = Player.findById(homePlayerId);
            Player awayPlayer = Player.findById(awayPlayerId);
            Game game = new Game(season, homePlayer, awayPlayer, homePoints, awayPoints, null);
            game.save();

            return ok(Json.toJson(game));
        }
    }

    public static Result updateGame(long seasonId, long id) {
        JsonNode json = request().body().asJson();

        Game game = Game.findById(id);
        if (seasonId != game.season.id) {
            return badRequest();
        }

        long homePlayerId = json.get("homePlayerId").longValue();
        long awayPlayerId = json.get("awayPlayerId").longValue();
        long homeTeamId = json.get("homeTeamId").longValue();
        long awayTeamId = json.get("awayTeamId").longValue();
        int homePoints = json.get("homePoints").intValue();
        int awayPoints = json.get("awayPoints").intValue();

        if (homeTeamId > 0) {
            Team homeTeam = Team.findById(homeTeamId);
            Team awayTeam = Team.findById(awayTeamId);
            game.homeTeam = homeTeam;
            game.awayTeam = awayTeam;
        } else {
            Player homePlayer = Player.findById(homePlayerId);
            Player awayPlayer = Player.findById(awayPlayerId);
            game.homePlayer = homePlayer;
            game.awayPlayer = awayPlayer;
            game.homePoints = homePoints;
            game.awayPoints = awayPoints;
        }

        game.homePoints = homePoints;
        game.awayPoints = awayPoints;
        game.save();
        return ok(Json.toJson(game));
    }

    public static Result deleteGame(long seasonId, long id) {
        Game game = Game.findById(id);
        if (seasonId != game.season.id) {
            return badRequest();
        }
        game.delete();
        return ok();
    }

}
