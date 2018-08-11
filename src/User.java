import java.sql.*;

public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private Database DB;
    private Semester[];

    User() {

    }

    User(int userID) {

    }

    User(String email) {

    }

    User (String email, String password){
        //Loop until the log in details are correct
        DB = new Database();
        String query = "SELECT userID, firstName FROM user WHERE email = " + email + " AND password = " + password + ";";
        ResultSet rs = null
        rs = DB.filterDB(query);
        if (rs = null) {
            // Login Failure
            ///break ;
            return; //Not sure which method will succeed in exiting
        }

        try {
            name = rs.getString("firstName");
            userID = rs.getInt("user");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is an error at try catch log in sequence");
        }
        //At this point we should have the userID from the login email
        //Or throw an error message if the password and the email are incorrect
        //Also got the name for the user

        query = "SELECT * FROM semester WHERE userID = " + userID + ";";
        rs = DB.filterDB(query);
        while (rs.next()){
            //Create Semester object
        }
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

