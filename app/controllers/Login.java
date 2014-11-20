package controllers;

import models.Player;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.login;
import views.html.logout;

import static play.data.Form.form;

public class Login extends Controller {

    public static Result login() {
        return Results.ok(login.render(form(LoginCredentials.class)));
    }

    public static Result authenticate() {
        Form<LoginCredentials> loginForm = form(LoginCredentials.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("_t", loginForm.get().player.createAuthToken());
            return redirect(routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        return Results.ok(logout.render());
    }

    public static class LoginCredentials {

        public String username;
        public String password;

        private Player player;

        public String validate() {
            player = Player.findByEmailAndPassword(username, password);
            return player == null ? "Invalid username or password" : null;
        }

    }

}
