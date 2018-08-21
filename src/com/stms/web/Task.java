package com.stms.web;

import java.util.Date;
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
    private String name;
    private Timestamp deadline;
    private boolean complete;
    private Integer priority;
    private String note;

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
            this.name = rs.getString("taskName");
            this.deadline = rs.getTimestamp("deadline");
            this.complete = rs.getBoolean("complete");
            this.priority = rs.getInt("priority");
            this.note = rs.getString("note");
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No Semester exists with the semesterID " + taskID);
        }
    }

    // METHODS //

    public Integer getTaskID () { return this.taskID;}

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
        this.recordSaved = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.recordSaved = false;
    }

    public Timestamp getDeadline() {
        return this.deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
        this.recordSaved = false;
    }

    public boolean getCompleted() { return this.complete;}

    public void setCompleted(boolean bool) {
        this.complete = bool;
        this.recordSaved = false;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        this.recordSaved = false;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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
            sql = "INSERT INTO task (userID, taskName, deadline, complete, priority, note) VALUES (?, ?, ?, ?, ?, ?)";
        } else{
            sql = "UPDATE task SET userID = ?, taskName = ?, deadline = ?, complete = ?, priority = ?, note = ? WHERE taskID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[6];
            types = new int[6];
        }else{
            params = new Object[7];
            types = new int[7];
            params[6] = this.taskID;
            types[6] = Types.INTEGER;
        }
        params[0] = this.userID;
        types[0] = Types.INTEGER;
        params[1] = this.name;
        types[1] = Types.VARCHAR;
        params[2] = this.deadline;
        types[2] = Types.TIMESTAMP;
        params[3] = this.complete;
        types[3] = Types.BOOLEAN;
        params[4] = this.priority;
        types[4] = Types.INTEGER;
        params[6] = this.note;
        types[6] = Types.VARCHAR;
        // execute query
        if(Database.update(sql, params, types)) {
            // get task ID
            sql = "SELECT taskID FROM task WHERE userID = ? AND name = ? AND deadline = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.userID;
            types[0] = Types.INTEGER;
            params[1] = this.name;
            types[1] = Types.VARCHAR;
            params[2] = this.deadline;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the semesterID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
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
}
