package com.stms.web;

import java.sql.*;
import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.Random;

public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private Database DB;
    private LinkedList<Semester> semesterLinkedList;

    User() {
        //Create user method
        //Check if the user exists

    }

    User(int userID) {

    }

    User(String email) {
        //String s = genSalt();
        //System.out.println(s);
        //email only login
        //check password
        System.out.println("User constructor start");
        //Loop until the log in details are correct
        DB = new Database();

        //String query = "SELECT userID, firstName FROM stms1.user WHERE email = '" + email + "' AND password = '" + password + "';";
        String query = "SELECT userID, firstName FROM stms1.user WHERE email = '" + email + "';";
        ResultSet rs = null;
        rs = DB.filterDB(query);

        try {
            if (rs.next()){
                userID = rs.getInt(1);
            } else {
                //log in failed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is an error at try catch log in sequence");
        }
        System.out.println("userID: " + userID);
        System.out.println("Got user info from DB");
        HashPassword("1234","5");
        loadSemesterInfo();
        //At this point we should have the userID from the login email
        //Or throw an error message if the password and the email are incorrect
        //Also got the name for the user
    }

    public boolean checkLogin (String password) {
        String query = "SELECT (pwdHash, pwdSalt) FROM stms1.user WHERE userID = " + userID + ";";
        try {
            ResultSet rs = DB.filterDB(query);
            pwdHash = rs.getString(1);
            pwdSalt = rs.getString(2);
        } catch (SQLException e){
            System.out.println("Failed to get query data CheckLogin method");
            e.printStackTrace();
        }
        String checkPassword = HashPassword(password, pwdSalt );
        if (checkPassword.equals(pwdHash)){
            //loadSemesterInfo();
            return true;
        } else {
            //passwords don't match
            return false;
        }
        //After the checkLogin is completed we call the rest of the DB stuff we need
        //
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
        String query = "SELECT * FROM stms1.semester WHERE userID = '" + userID + "';";
        ResultSet rs = DB.filterDB(query);
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

    public void CreateSemester(){//String name, Date start, Date end){

        //Date start = new Date(01/01/18);
        //Date end = new Date(30/06/18);
        //String query = "INSERT INTO stms1.semester VALUES (1," + userID +"'2018 Jan-June',";
        //PreparedStatement prep;
        //basic SQL statement for semester create INSERT INTO STMS1.USER (userID,firstName,lastNames,email,confirmed,userPassword)
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

