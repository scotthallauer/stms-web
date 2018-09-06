package com.stms.web;

import java.sql.*;

/**
 * Task class for Student Time Management System
 * Used to create new and edit existing tasks in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 20/08/2018
 */
public class Task {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer taskID;
    private Integer userID;
    private String description;
    private Timestamp dueDate;
    private Boolean complete;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new task records in the database.
     */
    public Task(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch an existing task from the database.
     * @param taskID the task's unique ID in the database
     */
    public Task (int taskID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get task details (if task exists)
        String sql = "SELECT * FROM task WHERE taskID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = taskID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.taskID = rs.getInt("taskID");
            this.userID = rs.getInt("userID");
            this.description = rs.getString("description");
            this.dueDate = rs.getTimestamp("dueDate");
            this.complete = rs.getBoolean("complete");
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No Task exists with the taskID " + taskID);
        }
    }

    /**
     * Getters and setters for this class
     */

    public Integer getTaskID () { return this.taskID;}

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
        this.recordSaved = false;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.recordSaved = false;
    }

    public Timestamp getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
        this.recordSaved = false;
    }

    public Boolean isComplete() { return this.complete;}

    public void setComplete(Boolean complete) {
        this.complete = complete;
        this.recordSaved = false;
    }

    /**
     * Save the tasks's details to the database.
     * @return true if successful, false otherwise.
     */
    public boolean save() {
        // check if database is connected
        if (!Database.isConnected()) {
            return false;
        }
        // don't need to save to database, there have been no changes
        if (this.recordSaved) {
            return true;
        }
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the taskID thereafter), then cannot save
        if (this.recordExists && this.taskID == null) {
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if (!this.recordExists) {
            sql = "INSERT INTO task (userID, description, dueDate, complete) VALUES (?, ?, ?, ?)";
        } else{
            sql = "UPDATE task SET userID = ?, description = ?, dueDate = ?, complete = ? WHERE taskID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[4];
            types = new int[4];
        }else{
            params = new Object[5];
            types = new int[5];
            params[4] = this.taskID;
            types[4] = Types.INTEGER;
        }
        params[0] = this.userID;
        types[0] = Types.INTEGER;
        params[1] = this.description;
        types[1] = Types.VARCHAR;
        params[2] = this.dueDate;
        types[2] = Types.TIMESTAMP;
        params[3] = this.complete;
        types[3] = Types.BOOLEAN;
        // execute query
        if(Database.update(sql, params, types)) {
            // get task ID
            sql = "SELECT taskID FROM task WHERE userID = ? AND description = ? AND dueDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.userID;
            types[0] = Types.INTEGER;
            params[1] = this.description;
            types[1] = Types.VARCHAR;
            params[2] = this.dueDate;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the taskID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.taskID = rs.getInt("taskID");
                }
            }catch (Exception e){}
            this.recordExists = true;
            this.recordSaved = true;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Delete the task's details from the database.
     * @return true if successful, false otherwise.
     */
    public boolean delete() {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM task WHERE taskID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.taskID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            return true;
        } else {
            System.out.println("Failed to delete task for taskID: " + this.taskID + ".");
            return false;
        }
    }
}
