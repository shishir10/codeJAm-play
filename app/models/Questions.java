/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.data.format.Formats;
import play.data.validation.Constraints;


/**
 *
 * @author shishir
 */
@Entity
public class Questions extends Model {
    
    @Id
    public Long id;
    
    @Constraints.Required
    @Formats.NonEmpty
    public String question;
    
    @Constraints.Required
    @Formats.NonEmpty
    public String link;
    
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Calendar dateCreation = Calendar.getInstance();
    
    public static Model.Finder<Long, Questions> find = new Model.Finder<Long, Questions>(Long.class, Questions.class);
    
    public static Questions getQuestion(){
        Long id = 1L;
        System.out.println(Questions.find.byId(id).question);
        return Questions.find.byId(id);
    }
    
}
