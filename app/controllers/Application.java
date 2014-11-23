package controllers;

import filters.LoginRequired;
import models.Player;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

@LoginRequired
public class Application extends Controller {

    public static Result index() {
        Player player = Player.findByAuthToken(session().get("_t"));
        return ok(index.render(player));
    }

}
