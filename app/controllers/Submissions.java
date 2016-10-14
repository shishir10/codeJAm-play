/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public Result submissions() {
        List<models.submissions> sub = models.submissions.findByUser(Users.findByEmail(request().username()));
//        Map<String, Integer> answers = new HashMap<>();
        int atempt = 0;
        for (models.submissions s : sub) {
//            answers.put(s.columnValue, s.answer);
            if (s.attempt > atempt) {
                atempt = s.attempt;
            }
        }
        return ok(submissions.render(Users.findByEmail(request().username()), atempt, sub, form(Submit.class)));
    }

    public Result uploadCsv() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            System.out.print(file.getName());
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
                System.out.print(sub);
                System.out.print(datamap.toString());
                for (models.submissions s : sub) {
//            answers.put(s.columnValue, s.answer);
                    if (s.attempt > attempt) {
                        attempt = s.attempt;
                    }
                }
                attempt++;
                if (sub.isEmpty()) {
                    attempt = 1;
                }
                for (String str : keys){
                    models.submissions subm = new models.submissions();
                    subm.user = Users.findByEmail(request().username());
                    subm.columnValue = str;
                    subm.answer = datamap.get(str);
                    subm.attempt = attempt;
                    subm.save();
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Submissions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Submissions.class.getName()).log(Level.SEVERE, null, ex);
            }
            flash("success", "File Uploaded !!");
            return redirect(routes.Submissions.submissions());
//            return ok(Users.findByEmail(request().username()).email + "<br>" + attempt);

        } else {
            flash("danger", "Select a file !");
            return redirect(routes.Submissions.submissions());
        }
    }
}
