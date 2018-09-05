package com.stms.web;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.sql.*;
import static java.sql.Types.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.Random;

/**
 * User class for Student Time Management System
 * Used to create new and edit existing user accounts in the database.
 * Offers additional functionality such as login verfication.
 * @author Scott Hallauer, Jonathon Everatt and Jessica Bourn
 * @version 18/08/2018
 */
public class User {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer userID;
    private String firstName;
    private String lastName;
    private String email;
    private String pwdHash;
    private String pwdSalt;
    private String tokenCode;
    private Timestamp tokenDate;
    private Semester[] semesters;
    private Task[] tasks;

    // CONSTRUCTOR //

    /**
     * Blank constructor used to create and insert new user accounts in the database.
     */
    public User() {
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor to create a user using their email. If an account with the provided email does not exist, an exception will be thrown.
     * @param email the account email address of an existing user
     */
    public User(String email) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get user account details (if user exists)
        String sql = "SELECT * FROM user WHERE email = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = email;
        types[0] = Types.VARCHAR;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.userID = rs.getInt("userID");
            this.firstName = rs.getString("firstName");
            this.lastName = rs.getString("lastName");
            this.email = rs.getString("email");
            this.pwdHash = rs.getString("pwdHash");
            this.pwdSalt = rs.getString("pwdSalt");
            this.tokenCode = rs.getString("tokenCode");
            if(rs.wasNull()) this.tokenCode = null;
            this.tokenDate = rs.getTimestamp("tokenDate");
            if(rs.wasNull()) this.tokenDate = null;
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No User exists with the email " + email);
        }
    }

    // METHODS //

    /**
     * Loads all of the semesters for the user into an array stored as an attribute.
     */
    private void loadSemesters() {
        // check if database is connected
        if(!Database.isConnected()) {
            return;
        }
        String sql = "SELECT * FROM semester WHERE userID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.userID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        ArrayList<Semester> arr = new ArrayList<Semester>();
        try {
            while(rs.next()){
                arr.add(new Semester(rs.getInt("semesterID")));
            }
        } catch (Exception e){
            System.out.println("Failed to load all semesters for User (userID: " + this.userID + ").");
            e.printStackTrace();
        }
        this.semesters = arr.toArray(new Semester[0]);
    }

    public Semester[] getSemesters(){
        this.loadSemesters();
        if(this.semesters == null){
            this.semesters = new Semester[0];
        }
        return this.semesters;
    }

    /**
     * Loads all of the tasks for the user into an array stored as an attribute.
     */
    private void loadTasks() {
        // check if database is connected
        if(!Database.isConnected()) {
            return;
        }
        String sql = "SELECT * FROM task WHERE userID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.userID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        ArrayList<Task> arr = new ArrayList<Task>();
        try {
            while(rs.next()){
                arr.add(new Task(rs.getInt("taskID")));
            }
        } catch (Exception e){
            System.out.println("Failed to load all tasks for User (userID: " + this.userID + ").");
            e.printStackTrace();
        }
        this.tasks = arr.toArray(new Task[0]);
    }

    public Task[] getTasks(){
        this.loadTasks();
        if(this.tasks == null){
            this.tasks = new Task[0];
        }
        return this.tasks;
    }

    public Integer getUserID(){
        return this.userID;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
        this.recordSaved = false;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
        this.recordSaved = false;
    }

    public String getInitials(){
        return String.valueOf(this.firstName.charAt(0)) + String.valueOf(this.lastName.charAt(0));
    }

    public String getEmail(){
        return this.email;
    }

    public boolean setEmail(String email){
        if(Utilities.validateEmail(email)) {
            this.email = email;
            this.recordSaved = false;
            return true;
        }else{
            return false;
        }
    }

    public String getTokenCode() {
        return this.tokenCode;
    }

    public Timestamp getTokenDate(){
        return this.tokenDate;
    }

    private void generateToken(){
        this.tokenCode = Utilities.randomString(64);
        this.tokenDate = Utilities.getCurrentTimestamp();
        this.recordSaved = false;
    }

    private void clearToken(){
        if(this.tokenCode != null || this.tokenDate != null) {
            this.tokenCode = null;
            this.tokenDate = null;
            this.recordSaved = false;
        }
    }

    /**
     * Checks if the supplied password matches the hashed password in the database.
     * @param plaintext the plaintext password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String plaintext) {
        try {
            String checkHash = Utilities.hashPassword(plaintext, this.pwdSalt);
            String realHash = this.pwdHash;
            return checkHash.equals(realHash);
        }catch (Exception e){
            return false;
        }
    }

    public boolean setPassword(String plaintext){
        try {
            String salt = Utilities.randomString(15);
            String hash = Utilities.hashPassword(plaintext, salt);
            this.pwdSalt = salt;
            this.pwdHash = hash;
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean forgotPassword () {
        this.generateToken();
        if(this.save()) {
            // SEND EMAIL WITH TOKEN
            return true;
        }else{
            return false;
        }
    }

    /**
     * Save the user's details to the database.
     * @return true if successful, false otherwise.
     */
    public boolean save(){
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // don't need to save to database, there have been no changes
        if(this.recordSaved){
            return true;
        }
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the userID thereafter), then cannot save
        if(this.recordExists && this.userID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists){
            sql = "INSERT INTO user (firstName, lastName, email, pwdHash, pwdSalt, tokenCode, tokenDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }else{
            sql = "UPDATE user SET firstName = ?, lastName = ?, email = ?, pwdHash = ?, pwdSalt = ?, tokenCode = ?, tokenDate = ? WHERE userID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[7];
            types = new int[7];
        }else{
            params = new Object[8];
            types = new int[8];
            params[7] = this.userID;
            types[7] = Types.INTEGER;
        }
        params[0] = this.firstName;
        types[0] = Types.VARCHAR;
        params[1] = this.lastName;
        types[1] = Types.VARCHAR;
        params[2] = this.email;
        types[2] = Types.VARCHAR;
        params[3] = this.pwdHash;
        types[3] = Types.VARCHAR;
        params[4] = this.pwdSalt;
        types[4] = Types.VARCHAR;
        params[5] = this.tokenCode;
        types[5] = Types.VARCHAR;
        params[6] = this.tokenDate;
        types[6] = Types.TIMESTAMP;
        // execute query
        if(Database.update(sql, params, types)){
            // get user ID
            sql = "SELECT userID FROM user WHERE email = ?";
            params = new Object[1];
            types = new int[1];
            params[0] = this.email;
            types[0] = Types.VARCHAR;
            ResultSet rs = Database.query(sql, params, types); // if fetching the userID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.userID = rs.getInt("userID");
                }
            }catch (Exception e){}
            this.recordExists = true;
            this.recordSaved = true;
            return true;
        }else{
            return false;
        }
    }
}

