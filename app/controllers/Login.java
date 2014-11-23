package controllers;

import models.Player;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.login;
import views.html.logout;
import views.html.register;

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

    public static Result register() {
        return Results.ok(register.render(form(RegistrationForm.class)));
    }

    public static Result registerNewUser() {
        Form<RegistrationForm> form = form(RegistrationForm.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(register.render(form));
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

    public static class LoginCredentials {

        public String username;
        public String password;

        private Player player;

        public String validate() {
            player = Player.findByEmailAndPassword(username, password);
            return player == null ? "Invalid username or password" : null;
        }

    }

    public static class RegistrationForm {

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
