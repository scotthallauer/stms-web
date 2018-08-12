import java.sql.*;

public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private Database DB;
    private Semester[];

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

    User() { }

    User(int userID) {

    }

    User(String email) {

    }

     User (String email, String password) { //This is for the log in sequence not create account
        System.out.println("User constructor start");
        //Loop until the log in details are correct
        DB = new Database(email, password);

        //String query = "SELECT userID, firstName FROM stms1.user WHERE email = '" + email + "' AND password = '" + password + "';";
        String query = "SELECT userID, firstName FROM stms1.user WHERE email = '" + email + "' AND userPassword = '" + password + "';";
        ResultSet rs = null;
        rs = DB.filterDB(query);

        try {
            if (rs.next()){
                userID = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is an error at try catch log in sequence");
        }
        System.out.println("userID: " + userID);
        //At this point we should have the userID from the login email
        //Or throw an error message if the password and the email are incorrect
        //Also got the name for the user
        loadSemesterInfo();


    }

    private void loadSemesterInfo(){
        String query = "SELECT * FROM stms1.semester WHERE userID = '" + userID + "';";
        ResultSet rs = DB.filterDB(query);
        System.out.println("Before trry catch block load semester info");
        try{
            while(rs.next()){
                System.out.println(rs.getString(1) + "SemesterID with user ID" + userID);
                Semester s = new Semester(rs.getString(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error User loadSemesterInfo");
        }
    }

    public void CreateSemester(){//String name, Date start, Date end){

        //Date start = new Date(01/01/18);
        //Date end = new Date(30/06/18);
        //String query = "INSERT INTO stms1.semester VALUES (1," + userID +"'2018 Jan-June',";
        //PreparedStatement prep;
        //basic SQL statement for semester create INSERT INTO STMS1.USER (userID,firstName,lastNames,email,confirmed,userPassword)
        //VALUES (1, 'Jonathon', 'Everatt', 'EVRJON003@myuct.ac.za', 1, '1234');
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

