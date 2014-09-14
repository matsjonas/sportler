package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Player;
import models.Team;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public class TeamResource {

    public static Result teams() {
        List<Team> teams = Team.findAll();
        return Results.ok(Json.toJson(teams));
    }

    public static Result team(Long id) {
        Team team = Team.findById(id);
        return Results.ok(Json.toJson(team));
    }

    public static Result createTeam() {
        JsonNode json = Controller.request().body().asJson();

        String name = json.get("name").textValue();
        long ownerId = json.get("ownerId").longValue();

        Player owner = Player.findById(ownerId);
        Team team = new Team(name, owner);
        team.save();

        return Results.ok(Json.toJson(team));
    }

    public static Result updateTeam(Long id) {
        JsonNode json = Controller.request().body().asJson();

        String name = json.get("name").textValue();
        long ownerId = json.get("ownerId").longValue();

        Team team = Team.findById(id);
        team.name = name;
        team.owner = Player.findById(ownerId);
        team.save();

        return Results.ok(Json.toJson(team));
    }

    public static Result deleteTeam(Long id) {
        Team team = Team.findById(id);
        team.delete();
        return Results.ok();
    }

    public static Result teamAdmins(long id) {
        Team team = Team.findById(id);
        return Results.ok(Json.toJson(team.admins));
    }

    public static Result addTeamAdmin(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Team team = Team.findById(id);
        team.addAdmin(player);
        team.save();

        return Results.ok(Json.toJson(team.admins));
    }

    public static Result deleteTeamAdmin(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Team team = Team.findById(id);
        team.removeAdmin(player);
        team.save();

        return Results.ok(Json.toJson(team.admins));
    }

    public static Result teamMembers(long id) {
        Team team = Team.findById(id);
        return Results.ok(Json.toJson(team.members));
    }

    public static Result addTeamMember(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Team team = Team.findById(id);
        team.addMember(player);
        team.save();

        return Results.ok(Json.toJson(team.members));
    }

    public static Result deleteTeamMember(long id) {
        JsonNode json = Controller.request().body().asJson();

        long userId = json.get("id").longValue();

        Player player = Player.findById(userId);
        Team team = Team.findById(id);
        team.removeMember(player);
        team.save();

        return Results.ok(Json.toJson(team.members));
    }

}
