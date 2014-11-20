package controllers;

import filters.LoginRequired;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

@LoginRequired
public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

}
