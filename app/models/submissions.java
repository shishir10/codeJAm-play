/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author shishir
 */
@Entity
public class submissions extends Model {
    @Id
    public Long id;
    
    @ManyToOne
    public Users user;
    
    public String columnValue;
    
    public int answer;
    
    public int attempt;
    
    Calendar dateCreated = Calendar.getInstance();
    
    public static Model.Finder<Long, submissions> find = new Model.Finder<>(Long.class, submissions.class);
    
    public static List<submissions> findByUser(Users user) {
        return find.where().eq("user", user).findList();
    }
}
