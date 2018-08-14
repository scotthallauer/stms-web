package com.stms.web;

import java.sql.*;

/**
 * Database class for Student Time Management System
 * Used to connect to MySQL database and execute SQL query and update statements
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 14/08/2018
 */
public class Database{

    private Connection conn = null;

    /**
     * Blank constructor which attempts to set up connection with database
     */
    public Database(){
        try {
            this.connect();
            System.out.println("Database connection successful.");
            this.query("USE stms;");
        } catch (Exception e){
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }

    }

    /**
     * Internal method which attempts to connect to database (using hard-coded connection details)
     * @throws Exception unsuccessful in connecting to database
     */
    private void connect() throws Exception{
        // load the MySQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // MySQL database log-in details
        String url = "jdbc:mysql://localhost:3306/stms";
        String user = "root";
        String password = "";
        // set up the connection (this will throw an exception if it fails to connect)
        this.conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * Method to test is the object has an active connection with the database
     * @return true if connected to the database, false otherwise
     */
    public boolean isConnected(){
        try{
            return this.conn.isValid(2);
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Method to execute a SQL query statement using the previously connected database
     * @param sql the SQL query statement to execute
     * @return the ResultSet from a successful query, null otherwise
     */
    public ResultSet query(String sql){
        ResultSet rs = null;
        try{
            PreparedStatement ps = this.conn.prepareStatement(sql);
            rs = ps.executeQuery();
        }catch(Exception e){
            System.out.println("Database query failed.");
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Method to execute a SQL update statement (i.e. INSERT, UPDATE, DELETE) using the previously connected database
     * @param sql the SQL update statement to execute
     */
    public void update(String sql){
        try{
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Database update failed.");
            e.printStackTrace();
        }
    }
}
