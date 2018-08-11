import java.sql.*;

public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private Database DB;

    User() {

    }

    User(int userID) {

    }

    User(String email) {

    }

    User (String email, String password){
        DB = new Database();
        String query ="SELECT userID FROM user WHERE email = " + email + " AND password = " + password;
        ResultSet rs = null
        rs = DB.filterDB(query);
        if (rs = null){
            // Login Failure
        }
        userID = rs.getInt("user");
        //At this point we should have the userID from the login email
        //Or throw an error message if the password and the email are incorrect
        
    }

    public boolean checkLogin (String password) {

    }

    public boolean forgotPassword (String email) {

    }

    public void addSemester (Semester semester) {

    }

    public Semester[] getSemesters() {

    }

    public boolean savetoDB() {

    }
}

