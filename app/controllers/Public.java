package controllers;

import models.Player;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.logout;
import views.html.registration;
import views.html.welcome;

import static play.data.Form.form;

public class Public extends Controller {

    public static Result welcome() {
        return Results.ok(welcome.render(form(LoginInfo.class)));
    }

    public static Result login() {
        Form<LoginInfo> loginForm = form(LoginInfo.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(welcome.render(loginForm));
        } else {
            session().clear();
            session("_t", loginForm.get().player.createAuthToken());
            return redirect(routes.Application.index());
        }
    }

    public static Result registration() {
        return Results.ok(registration.render(form(RegistrationInfo.class)));
    }

    public static Result register() {
        Form<RegistrationInfo> form = form(RegistrationInfo.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(registration.render(form));
        } else {
            Player player = new Player(form.get().email, form.get().name, form.get().password);
            player.save();
            session().clear();
            session("_t", player.createAuthToken());
            return redirect(routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        return Results.ok(logout.render());
    }

    public static class LoginInfo {

        public String email;
        public String password;

        private Player player;

        public String validate() {
            player = Player.findByEmailAndPassword(email, password);
            return player == null ? "Invalid email or password" : null;
        }

    }

    public static class RegistrationInfo {

        public String email;
        public String name;
        public String password;

        public String validate() {
            if (email == null && name == null && password == null) {
                return "All fields must be filled out";
            } else if (!Player.isEmailUnique(email)) {
                return "That email is already registered";
            } else {
                return null;
            }
        }

    }

}
