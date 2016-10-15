/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author shishir
 */
@Entity
public class Scorecard extends Model {
    @Id
    public Long id;
    
    @ManyToOne
    public Users user;
    
    @ManyToOne
    public Questions quest;
            
    public int attempt;
    public int marks;
    public int maxMarks;
    public float percent;
    Calendar dateCreated = Calendar.getInstance();
    
    public static Model.Finder<Long, Scorecard> find = new Model.Finder<>(Long.class, Scorecard.class);
    
}
