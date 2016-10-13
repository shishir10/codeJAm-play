package models;

import com.avaje.ebean.ExpressionList;
import models.utils.AppException;
import models.utils.Hash;
import play.data.format.Formats;
import play.data.validation.Constraints;
import com.avaje.ebean.Model;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: yesnault Date: 20/01/12
 */
@Entity
public class Users extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String email;

    @Constraints.Required
    @Formats.NonEmpty
    public String fullname;

    @Constraints.Required
    @Formats.NonEmpty
    public String college;

    @Constraints.Required
    @Formats.NonEmpty
    public String branch;

    @Constraints.Required
    @Formats.NonEmpty
    public int graduationYear;

    @Constraints.Required
    @Formats.NonEmpty
    public Long phoneNumber;

    public String confirmationToken;

    @Constraints.Required
    @Formats.NonEmpty
    public String passwordHash;

    Calendar cal = Calendar.getInstance();

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation = cal.getTime();

    @Formats.NonEmpty
    public Boolean validated = false;

    // -- Queries (long id, user.class)
    public static Model.Finder<Long, Users> find = new Model.Finder<>(Long.class, Users.class);

    /**
     * Retrieve a user from an email.
     *
     * @param email email to search
     * @return a user
     */
    public static Users findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static Users findByPhoneNumber(Long phNumber) {
        return find.where().eq("phoneNumber", phNumber).findUnique();
    }

    /**
     * Retrieve a user from a fullname.
     *
     * @param fullname Full name
     * @return a user
     */
    public static Users findByFullname(String fullname) {
        return find.where().eq("fullname", fullname).findUnique();
    }

    public static ExpressionList<Users> findByCollege(String college) {
        return find.where().eq("college", college);
    }

    /**
     * Retrieves a user from a confirmation token.
     *
     * @param token the confirmation token to use.
     * @return a user if the confirmation token is found, null otherwise.
     */
    public static Users findByConfirmationToken(String token) {
        return find.where().eq("confirmationToken", token).findUnique();
    }

    /**
     * Authenticate a User, from a email and clear password.
     *
     * @param email email
     * @param clearPassword clear password
     * @return User if authenticated, null otherwise
     * @throws AppException App Exception
     */
    public static Users authenticate(String email, String clearPassword) throws AppException {

        // get the user with email only to keep the salt password
        Users user = find.where().eq("email", email).findUnique();
        if (user != null) {
            // get the hash password from the salt + clear password
            if (Hash.checkPassword(clearPassword, user.passwordHash)) {
                return user;
            }
        }
        return null;
    }

    public void changePassword(String password) throws AppException {
        this.passwordHash = Hash.createPassword(password);
        this.save();
    }

    /**
     * Confirms an account.
     *
     * @return true if confirmed, false otherwise.
     * @throws AppException App Exception
     */
    public static boolean confirm(Users user) throws AppException {
        if (user == null) {
            return false;
        }

        user.confirmationToken = null;
        user.validated = true;
        user.save();
        return true;
    }

    public static Map<String, Boolean> listusers() {
        Map<String, Boolean> userMap = new TreeMap<>();
        userMap.put("test1", Boolean.FALSE);
        userMap.put("test2", Boolean.FALSE);
        userMap.put("test3", Boolean.FALSE);
        userMap.put("test4", Boolean.FALSE);
        return userMap;
    }
}
