package com.stms.web;

import java.sql.*;
import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.Random;

/**
 * User class for Student Time Management System
 * Used to create new and edit existing user accounts in the database.
 * Offers additional functionality such as login verfication.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 16/08/2018
 */
public class User {

    // ATTRIBUTES //

    private int userID;
    private String firstName;
    private String lastNames;
    private String email;
    private boolean activated;
    private String pwdHash;
    private String pwdSalt;
    private Semester[] semesters;

    // CONSTRUCTOR //

    /**
     * Blank constructor used to create and insert new user accounts in the database.
     */
    public User() {}

    /**
     * Parameterised constructor to create a user using their email. If an account with the provided does not exist, an exception will be thrown.
     * @param email the account email address of an existing user
     */
    public User(String email) throws Exception {
        // connect to database
        System.out.println("User constructor has been called.");
        Database db = new Database();
        if(db.isConnected()) {
            // query database to get user account details (if user exists)
            ResultSet rs = db.query("SELECT * FROM user WHERE email = '" + email + "';");
            if (rs.first()) {
                this.userID = rs.getInt("userID");
                this.firstName = rs.getString("firstName");
                this.lastNames = rs.getString("lastNames");
                this.email = rs.getString("email");
                this.activated = rs.getBoolean("activated");
                this.pwdHash = rs.getString("pwdHash");
                this.pwdSalt = rs.getString("pwdSalt");
            }else{
                throw new NullPointerException();
            }
            System.out.println("Successfully loaded User (userID: " + userID + ") from database.");
            this.loadSemesters();
        }else{
            throw new SQLException();
        }
        System.out.println("User object has been constructed");
    }

    // METHODS //

    /**
     * Loads all of the semesters for the user into an array stored as an attribute.
     */
    private void loadSemesters(){
        Database db = new Database();
        String sql = "SELECT * FROM semester WHERE userID = " + this.userID + ";";
        ResultSet rs = db.query(sql);
        try {
            // set length of array
            if(rs.last()){
                this.semesters = new Semester[rs.getRow()];
            }
            int count = 0;
            if(rs.first()){
                do{
                    this.semesters[count] = new Semester(rs.getInt("semesterID"));
                    count++;
                }while(rs.next());
            }
        } catch (Exception e){
            System.out.println("Failed to load all semesters for user (userID: " + this.userID + ")");
            e.printStackTrace();
        }
    }

    /**
     * Checks if the supplied password matches the hashed password in the database.
     * @param password the plaintext password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        String checkPassword = HashPassword(password, this.pwdSalt);
        return checkPassword.equals(this.pwdHash);
    }

    /**
     * This method is used to hash a password that the user inputs
     * It is used in both create account and log in use case.
     * Uses SHA - 256 hashing to hash a password
     *
     * @param Hash password that will be hashed
     * @param Salt Salt for the user gotten from the DB
     * @return Hashed password including salt
     */
    public String HashPassword(String Hash, String Salt){
        // arbitrary decision to put salt at the end
        String pass = Hash + Salt;
        // Hash algorithm gotten from
        // https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

	/**
     * Used in create an account sequence to generate a random salt for the user's account
     * @return the unique salt to be used for password hashing
     */
    public String genSalt(){
        byte[] salt = new byte[8];
        Random r = new Random();
        r.nextBytes(salt);
        String Salt ="";
        for (int x = 0; x < 8; x++){
            Salt = Salt + salt[x];
        }
        System.out.println("The Salt key has been generated");

        return Salt;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastNames(){
        return this.lastNames;
    }

    public void setLastNames(String lastNames){
        this.lastNames = lastNames;
    }

    public String getInitials(){
        return String.valueOf(this.firstName.charAt(0)) + String.valueOf(this.lastNames.charAt(0));
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public boolean isActivated(){
        return this.activated;
    }

    /**
     * Save the user's details to the database.
     * @return true if successful, false otherwise.
     */
    private boolean saveToDB(){
        Database db = new Database();
        if(db.isConnected()) {
            String sql = "INSERT INTO user (firstName,lastNames,email,activated,pwdHash,pwdSalt) " +
                    "VALUES ('" + this.firstName + "','" + this.lastNames + "','" + this.email + "'," + this.activated + ",'" + this.pwdHash + "','" + this.pwdSalt + "');";
            db.update(sql);
            return true;
        }else{
            return false;
        }
    }

    // Methods still to be implemented
    /*
    public boolean forgotPassword (String email) {
    }

    public Semester[] getSemesters() {
    }
    */
}

