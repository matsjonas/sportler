package filters;

import controllers.routes;
import models.Player;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

public class LoginRequiredFilter extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        String token = ctx.session().get("_t");
        if (token != null) {
            Player player = Player.findByAuthToken(token);
            if (player != null) {
                ctx.request().setUsername(player.getEmail());
                return delegate.call(ctx);
            }
        }
        Result redirect = Results.redirect(routes.Public.welcome());
        return F.Promise.pure(redirect);
    }

}
