package com.stms.web;

import java.sql.*;

/**
 * Database class for Student Time Management System
 * Used to connect to MySQL database and execute SQL query and update statements
 * @author Scott Hallauer, Jonathon Everatt and Jessica Bourn
 * @version 18/08/2018
 */
public class Database{

    private static Connection conn = null;

    /**
     * Static initializer which sets up a single connection with database for the server (shared amongst all sessions).
     */
    static{
        try {
            connect();
            System.out.println("INFO: Database connection successful.");
            query("USE stms;"); // ensure that all queries are using the stms schema
        } catch (Exception e){
            System.out.println("ERROR: Database connection failed.");
            e.printStackTrace();
        }
    }

    /**
     * Internal method which attempts to connect to database (using hard-coded connection details).
     * @throws Exception unsuccessful in connecting to database
     */
    private static void connect() throws Exception{
        // load the MySQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // MySQL database log-in details
        String url = "jdbc:mysql://localhost:3306/stms";
        String user = "root";
        String password = "";
        // set up the connection (this will throw an exception if it fails to connect)
        conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * Method to test if the object has an active connection with the database.
     * @return true if connected to the database, false otherwise
     */
    public static boolean isConnected(){
        try{
            return conn.isValid(2);
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Method to get the database Connection object (this can be used to pass the connection to other libraries which need to use it)
     * @return the database connection
     */
    public static Connection getConnection(){
        return conn;
    }

    /**
     * Method to execute a SQL query statement (with no parameters) using the previously connected database.
     * @param sql the SQL query statement to execute
     * @return the ResultSet from a successful query, null otherwise
     */
    public static ResultSet query(String sql){
        return query(sql, new Object[0], new int[0]);
    }

    /**
     * Method to execute a SQL query statement (with parameters) using the previously connected database.
     * @param sql the SQL query statement to execute
     * @param params the statement parameters to include in the query
     * @param types the SQL types (java.sql.Types) of the statement parameters
     * @return the ResultSet from a successful query, null otherwise
     */
    public static ResultSet query(String sql, Object[] params, int[] types){
        ResultSet rs = null;
        try{
            if(params.length != types.length){
                throw new IllegalArgumentException("The params and types arrays must be of equal length.");
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            for(int i = 0 ; i < params.length ; i++){
                if(params[i] == null){
                    ps.setObject(i+1, params[i], Types.NULL);
                }else{
                    ps.setObject(i+1, params[i], types[i]);
                }
            }
            rs = ps.executeQuery();
        }catch(Exception e){
            System.out.println("ERROR: Database query failed.");
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Method to execute a SQL update statement (i.e. INSERT, UPDATE, DELETE), with no parameters, using the previously connected database.
     * @param sql the SQL update statement to execute
     * @return true if database update is successful, false otherwise
     */
    public static boolean update(String sql){
        return update(sql, new Object[0], new int[0]);
    }

    /**
     * Method to execute a SQL update statement (i.e. INSERT, UPDATE, DELETE), with parameters, using the previously connected database.
     * @param sql the SQL update statement to execute
     * @param params the statement parameters to include in the query
     * @param types the SQL types (java.sql.Types) of the statement parameters
     * @return true if database update is successful, false otherwise
     */
    public static boolean update(String sql, Object[] params, int[] types){
        try{
            if(params.length != types.length){
                throw new IllegalArgumentException("The params and types arrays must be of equal length.");
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            for(int i = 0 ; i < params.length ; i++){
                if(params[i] == null){
                    ps.setObject(i+1, params[i], Types.NULL);
                }else{
                    ps.setObject(i+1, params[i], types[i]);
                }
            }
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println("ERROR: Database update failed.");
            e.printStackTrace();
            return false;
        }
    }
}
