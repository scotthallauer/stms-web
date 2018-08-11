import java.sql.*;

public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private Database DB;

    // getters and setters for all variables

    public void setUserID(int ID) {
        this.userID = ID; }

    public int getUserID() {
        return userID; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setEmail(String email) {
        this.email = email; }

    public String getEmail() {
        return email; }

    public void setPwdHash(String hash) {
        this.pwdHash = hash; }

    public String getPwdHash() {
        return pwdHash; }

    public void setPwdSalt(String salt) {
        this.pwdSalt = salt; }

    public String getPwdSalt() {
        return pwdSalt; }

    public void setDB(Database db) {
        this.DB = db; }

    public Database getDB() {
        return DB; }

    // Various constructors

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

