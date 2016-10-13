/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.data.format.Formats;
import play.data.validation.Constraints;

/**
 *
 * @author shishir
 */
@Entity
public class College extends Model {
    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String colName;
    
    
    public static Model.Finder<Long, College> find = new Model.Finder<>(Long.class, College.class);
    
    public static College findByName(String colNAme) {
        return find.where().eq("colName", colNAme).findUnique();
    }
    
     public static Map<String, Boolean> listCol() {
//        Map<String, Boolean> userMap = new TreeMap<>();
//        userMap.put("test1", Boolean.FALSE);
//        userMap.put("test2", Boolean.FALSE);
//        userMap.put("test3", Boolean.FALSE);
//        userMap.put("test4", Boolean.FALSE);
//        
        Map<String, Boolean> colmap = new TreeMap<>();
        for (College col : College.find.all()){
            colmap.put(col.colName, Boolean.FALSE);
        }
        
        return colmap;
    }
    
}
