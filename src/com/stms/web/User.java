package com.stms.web;

import java.sql.*;
import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.Random;

public class User {

    private Database db;
    private int userID;
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
            ResultSet rs = this.db.query("SELECT userID FROM user WHERE email = '" + email + "';");
            if (rs.first()) {
                this.userID = rs.getInt("userID");
            }else{
                throw new Exception();
            }
            System.out.println("userID: " + userID);
            System.out.println("Got user info from DB");
            HashPassword("password", "gh90845hg093hqp");
            //loadSemesterInfo();
            //At this point we should have the userID from the login email
            //Or throw an error message if the password and the email are incorrect
            //Also got the name for the user
        }else{
            throw new Exception();
        }
    }

    public boolean checkPassword(String password) {
        ResultSet rs = this.db.query("SELECT pwdHash, pwdSalt FROM user WHERE userID = " + this.userID + ";");
        try {
            if (rs.first()) {
                String pwdHash = rs.getString("pwdHash");
                String pwdSalt = rs.getString("pwdSalt");
                String checkPassword = HashPassword(password, pwdSalt);
                return checkPassword.equals(pwdHash);
            }else{
                return false;
            }
        } catch (Exception e){
            System.out.println("Failed to get query data CheckPassword method");
            e.printStackTrace();
            return false;
        }
    }

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

