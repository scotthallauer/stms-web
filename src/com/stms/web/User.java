package com.stms.web;

import java.sql.*;
import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.Random;

public class User {

    private Database db;
    private int userID;
    private String firstName;
    private String lastNames;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private LinkedList<Semester> semesterLinkedList;

    public User() {
        //Create user method
        //Check if the user exists
    }

    public User(int userID) {

    }

    /**
     * Parameterised constructor to create a user using their email. If an account with the provided does not exist, an exception will be thrown.
     * @param email the account email address of an existing user
     */
    public User(String email) throws Exception{
        // connect to database
        this.db = new Database();
        if(this.db.isConnected()) {
            // query database to get userID (if user exists)
            ResultSet rs = this.db.query("SELECT * FROM user WHERE email = '" + email + "';");
            if (rs.first()) {
                this.userID = rs.getInt("userID");
                this.firstName = rs.getString("firstName");
                this.lastNames = rs.getString("lastNames");
                this.email = rs.getString("email");
                this.pwdHash = rs.getString("pwdHash");
                this.pwdSalt = rs.getString("pwdSalt");
            }else{
                throw new Exception();
            }
            System.out.println("Successfully loaded User (userID: " + userID + ") from database.");
            //loadSemesterInfo();
            //At this point we should have the userID from the login email
            //Or throw an error message if the password and the email are incorrect
            //Also got the name for the user
        }else{
            throw new Exception();
        }
    }

    public boolean checkPassword(String password) {
        String checkPassword = HashPassword(password, this.pwdSalt);
        return checkPassword.equals(this.pwdHash);
    }
	
	/*
	 * This method is used to hash a password that the user inputs
	 * It is used in both create account and log in use case.
	 * Uses SHA - 256 hashing to hash a password
	*/

    public String HashPassword(String Hash, String Salt){
        //arbitrary decision to put salt at the end
        String pass = Hash + Salt;
        System.out.println(pass);
        //Hash algorithm gotten from
        //https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            System.out.println(hexString.toString());
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        //End of Hash algorithm
    }
	
	/*
     *  Load semester info class used to get the semester information for the user
     *  called after user validation and method leads to loading all the data from all the classes
     */

    private void loadSemesterInfo(){
        String sql = "SELECT * FROM semester WHERE userID = '" + userID + "';";
        ResultSet rs = db.query(sql);
        System.out.println("Load Semester method called and DB filtered");
        try{
            while(rs.next()){
                System.out.println(rs.getString(1) + "SemesterID with user ID: " + userID);
                Semester s = new Semester(rs.getInt(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error User loadSemesterInfo");
        }
        System.out.println("Semester info loaded");
    }

	/*
     * Used in create am account sequence to generate a random salt for the user's account
     */
	
    public String genSalt(){
        byte[] salt = new byte[16];
        Random r = new Random();
        r.nextBytes(salt);
        String Salt ="";
        for (int x = 0; x < 16; x++){
            Salt = Salt + salt[x];
        }
        System.out.println(Salt);
        return Salt;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastNames(){
        return this.lastNames;
    }

    public String getInitials(){
        return String.valueOf(this.firstName.charAt(0)) + String.valueOf(this.lastNames.charAt(0));
    }

    public String getEmail(){
        return this.email;
    }
	
	/*
     *  Save the information of the user class to the database
	 * used in the create account use case
     */

	private void saveToDB(String password){
        //password is plain text from the user
        /*String salt = genSalt();
        String DBPassword = HashPassword(password, salt);
        String sql = "INSERT INTO user ((firstName,lastNames,email,confirmed,pwdHash,pwdSalt) \n" +
                "VALUES ('" + name + "','','" + email +"',1,'" + DBPassword + "','" + salt + "');";
        db.update(sql);*/

    }

    public void CreateSemester(){//String name, Date start, Date end){

        //Date start = new Date(01/01/18);
        //Date end = new Date(30/06/18);
        //String query = "INSERT INTO semester VALUES (1," + userID +"'2018 Jan-June',";
        //PreparedStatement prep;
        //basic SQL statement for semester create INSERT INTO USER (userID,firstName,lastNames,email,confirmed,userPassword)
        //VALUES (1, 'Jonathon', 'Everatt', 'EVRJON003@myuct.ac.za', 1, '1234');
    }

   /*

    public boolean forgotPassword (String email) {

    }

    public Semester[] getSemesters() {

    }

    public boolean savetoDB() {

    }*/
}

