/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author shishir
 */
@Entity
public class Answers extends Model {
    
    @Id
    public Long id;
    
    @ManyToOne
    public Questions question;
    
    public String colVal;
    public int answer;
    Calendar dateCreated = Calendar.getInstance();
    
    public static Model.Finder<Long, Answers> find = new Model.Finder<>(Long.class, Answers.class);
    
}
