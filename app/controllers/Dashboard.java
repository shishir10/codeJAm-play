package controllers;

import java.util.List;
import models.Questions;
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
        List<Questions> ques = Questions.find.where().eq("is_active", true).findList();
        return ok(index.render(Users.findByEmail(request().username()), ques));
    }
}
