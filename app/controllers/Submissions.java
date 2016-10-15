/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Answers;
import models.Questions;
import models.Scorecard;
import models.Users;
import static play.data.Form.form;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Http;
import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Security;
import views.html.submissions.submissions;

/**
 *
 * @author shishir
 */
@Security.Authenticated(Secured.class)
public class Submissions extends Controller {

    public static class Submit {

        File file;
    }

    public Result submissions(Long id) {
        List<models.submissions> sub = models.submissions.findByUser(Users.findByEmail(request().username()));
//        Map<String, Integer> answers = new HashMap<>();
        int atempt = 0;
        for (models.submissions s : sub) {
//            answers.put(s.columnValue, s.answer);
            if (Objects.equals(s.quest.id, id) && s.attempt > atempt) {
                atempt = s.attempt;
            }
        }
        Scorecard card = Scorecard.find.where()
                .and(Expr.eq("user_id", Users.findByEmail(request().username()).id), 
                        Expr.eq("quest_id", id)).findUnique();
        System.out.println(card);
        return ok(submissions.render(Users.findByEmail(request().username()), atempt, sub, form(Submit.class), id, card));
    }

    public Result uploadCsv(Long id) {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");

        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            String[] b = null;
            Map<String, Integer> datamap = new HashMap<>();
            Set<String> keys = null;
            int attempt = 0;
            List<models.submissions> sub = models.submissions.findByUser(Users.findByEmail(request().username()));
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    b = line.split(",");
                    datamap.put(b[0], Integer.parseInt(b[1]));
                }
                keys = datamap.keySet();
                for (models.submissions s : sub) {
//            answers.put(s.columnValue, s.answer);
                    if (Objects.equals(s.quest.id, id) && s.attempt > attempt) {
                        attempt = s.attempt;
                    }
                }
                attempt++;
                if (sub.isEmpty()) {
                    attempt = 1;
                }
                for (String str : keys) {
                    models.submissions subm = new models.submissions();
                    subm.user = Users.findByEmail(request().username());
                    subm.quest = Questions.find.byId(id);
                    subm.columnValue = str;
                    subm.answer = datamap.get(str);
                    subm.attempt = attempt;
                    subm.save();
                }
                evaluate(Users.findByEmail(request().username()), Questions.find.byId(id), attempt);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Submissions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Submissions.class.getName()).log(Level.SEVERE, null, ex);
            }
            flash("success", "File Uploaded !!");
            return redirect(routes.Submissions.submissions(id));
//            return ok(Users.findByEmail(request().username()).email + "<br>" + attempt);

        } else {
            flash("danger", "Select a file !");
            return redirect(routes.Submissions.submissions(id));
        }
    }

    public void evaluate(Users user, Questions question, int attempt) {
        List<Answers> answer = Answers.find.all();
        Map<String, Integer> ansMap = new HashMap<>();
        Map<String, Integer> subMap = new HashMap<>();
        int score = 0;
        float percent = 0.00f;
        for (Answers ans : answer) {
            if (Objects.equals(ans.question.id, question.id)) {
                ansMap.put(ans.colVal, ans.answer);
            }
        }
        List<models.submissions> sublist = models.submissions.find.all();
        for (models.submissions sub : sublist) {
            if (Objects.equals(sub.user.id, user.id) && Objects.equals(sub.quest.id, question.id) && sub.attempt == attempt) {
                subMap.put(sub.columnValue, sub.answer);
            }
        }
        Set<String> keySet = ansMap.keySet();
        for (String key : keySet) {
            if (Objects.equals(ansMap.get(key.toLowerCase()), subMap.get(key.toLowerCase()))) {
                score++;
//                System.out.println(score);
            }
        }
        System.out.println(score);
        if (score == 0) {
            percent = 0.0f;
        } else {
            percent = ((float) score / (float) keySet.size()) * 100f;
        }
//        Scorecard card = Scorecard.find.where()
//                .and(Expr.eq("user", user), Expr.eq("quest", question)).findUnique();
        List<Scorecard> all = Scorecard.find.all();
        if (all.isEmpty()) {
            Scorecard card = new Scorecard();
            card.user = user;
            card.attempt = attempt;
            card.quest = question;
            card.marks = score;
            card.maxMarks = keySet.size();
            card.percent = percent;
            card.save();
        } else {
            for (Scorecard card : all) {
                if (Objects.equals(card.user.id, user.id) && Objects.equals(card.quest.id, question.id)) {
                    if (card.percent < percent) {
                        card.marks = score;
                        card.percent = percent;
                        card.attempt = attempt;
                        card.save();
                    }
                }
            }
        }
    }
}
