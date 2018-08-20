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
    private boolean completed;
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
    }
}
