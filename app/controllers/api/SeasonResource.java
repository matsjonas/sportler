package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Player;
import models.Season;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public class SeasonResource {

    public static Result seasons() {
        List<Season> seasons = Season.findAll();
        return Results.ok(Json.toJson(seasons));
    }

    public static Result season(Long id) {
        Season season = Season.findById(id);
        return Results.ok(Json.toJson(season));
    }

    public static Result createSeason() {
        JsonNode json = Controller.request().body().asJson();

        String name = json.get("name").textValue();
        JsonNode ownerJson = json.get("owner");
        long ownerId = ownerJson.get("id").longValue();

        Player owner = Player.findById(ownerId);
        Season season = new Season(name, owner);
        season.save();

        return Results.ok(Json.toJson(season));
    }

    public static Result updateSeason(Long id) {
        JsonNode json = Controller.request().body().asJson();

        String name = json.get("name").textValue();
        long ownerId = json.get("ownerId").longValue();
        boolean open = json.get("open").booleanValue();

        Season season = Season.findById(id);
        season.name = name;
        season.owner = Player.findById(ownerId);
        season.open = open;
        season.save();

        return Results.ok(Json.toJson(season));
    }

    public static Result deleteSeason(Long id) {
        Season season = Season.findById(id);
        season.delete();
        return Results.ok();
    }

    public static Result seasonAdmins(long id) {
        Season season = Season.findById(id);
        return Results.ok(Json.toJson(season.admins));
    }

    public static Result addSeasonAdmin(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Season season = Season.findById(id);
        season.addAdmin(player);
        season.save();

        return Results.ok(Json.toJson(season.admins));
    }

    public static Result deleteSeasonAdmin(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Season season = Season.findById(id);
        season.removeAdmin(player);
        season.save();

        return Results.ok(Json.toJson(season.admins));
    }

}
