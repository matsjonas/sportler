package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Player;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class PlayerResource extends Controller {

    public static Result players() {
        List<Player> players = Player.findAll();
        return ok(Json.toJson(players));
    }

    public static Result player(Long id) {
        Player player = Player.findById(id);
        return ok(Json.toJson(player));
    }

    public static Result createPlayer() {
        JsonNode json = request().body().asJson();

        String email = json.get("email").textValue();
        String name = json.get("name").textValue();
        String password = json.get("password").textValue();

        Player player = new Player(email, name, password);
        player.save();

        return ok(Json.toJson(player));
    }

    public static Result updatePlayer(Long id) {
        JsonNode json = request().body().asJson();

        String email = json.get("email").textValue();
        String name = json.get("name").textValue();

        Player player = Player.findById(id);
        player.setEmail(email);
        player.name = name;
        player.update(id);

        return ok(Json.toJson(player));
    }

    public static Result deletePlayer(Long id) {
        Player player = Player.findById(id);
        player.delete();
        return ok();
    }

}
