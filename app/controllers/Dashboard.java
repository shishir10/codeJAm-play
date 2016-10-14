package controllers;

import models.Users;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard.index;

/**
 * User: shishir
 * Date: 25/09/16
 */
@Security.Authenticated(Secured.class)
public class Dashboard extends Controller {

    public Result index() {
//        String question = Questions.getQuestion().question;
//        Html ques = Html.apply(question);
        return ok(index.render(Users.findByEmail(request().username())));
    }
}
