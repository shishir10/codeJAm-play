package controllers;

import models.Users;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard.index;

/**
 * User: yesnault
 * Date: 22/01/12
 */
@Security.Authenticated(Secured.class)
public class Dashboard extends Controller {

    public Result index() {
        return ok(index.render(Users.findByEmail(request().username())));
    }
}
