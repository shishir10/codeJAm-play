package controllers.account.settings;

import controllers.Secured;
import models.Token;
import models.Users;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.account.settings.email;
import views.html.account.settings.emailValidate;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import java.net.MalformedURLException;

import static play.data.Form.form;

/**
 * Settings -> Email page.
 * <p/>
 * User: shishir
 * Date: 23/06/12
 */
@Security.Authenticated(Secured.class)
public class Email extends Controller {
    @Inject
    MailerClient mailerClient;

    public static class AskForm {
        @Constraints.Required
        public String email;

        public AskForm() {
        }

        AskForm(String email) {
            this.email = email;
        }
    }

    /**
     * Password Page. Ask the user to change his password.
     *
     * @return index settings
     */
    public Result index() {
        Users user = Users.findByEmail(request().username());
        Form<AskForm> askForm = form(AskForm.class);
        askForm = askForm.fill(new AskForm(user.email));
        return ok(email.render(Users.findByEmail(request().username()), askForm));
    }

    /**
     * Send a mail to confirm.
     *
     * @return email page with flash error or success
     */
    public Result runEmail() {
        Form<AskForm> askForm = form(AskForm.class).bindFromRequest();
        Users user = Users.findByEmail(request().username());

        if (askForm.hasErrors()) {
            flash("error", Messages.get("signup.valid.email"));
            return badRequest(email.render(user, askForm));
        }

        try {
            String mail = askForm.get().email;
            Token t = new Token();
            t.sendMailChangeMail(user, mail,mailerClient);
            flash("success", Messages.get("changemail.mailsent"));
            return ok(email.render(user, askForm));
        } catch (MalformedURLException e) {
            Logger.error("Cannot validate URL", e);
            flash("error", Messages.get("error.technical"));
        }
        return badRequest(email.render(user, askForm));
    }

    /**
     * Validate a email.
     *
     * @return email page with flash error or success
     */
    public Result validateEmail(String token) {
        Users user = Users.findByEmail(request().username());

        if (token == null) {
            flash("error", Messages.get("error.technical"));
            return badRequest(emailValidate.render(user));
        }

        Token resetToken = Token.findByTokenAndType(token, Token.TypeToken.email);
        if (resetToken == null) {
            flash("error", Messages.get("error.technical"));
            return badRequest(emailValidate.render(user));
        }

        if (resetToken.isExpired()) {
            resetToken.delete();
            flash("error", Messages.get("error.expiredmaillink"));
            return badRequest(emailValidate.render(user));
        }

        user.email = resetToken.email;
        user.save();

        session("email", resetToken.email);

        flash("success", Messages.get("account.settings.email.successful", user.email));

        return ok(emailValidate.render(user));
    }
}
