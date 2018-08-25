package com.stms.web;

import java.util.Date;
import java.sql.*;

/**
 * CourseAssignment class for Student Time Management System
 * Used to create new and edit existing course assignments in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 19/08/2018
 */
public class CourseAssignment {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer assignmentID;
    private Integer courseID;
    private String name;
    private Timestamp dueDate;
    private Integer priority;
    private Double possibleMark;
    private Double earnedMark;
    private Double weighting;
    private String note;
    private boolean complete;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course assignment records in the database.
     */
    public CourseAssignment(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch any existing course assignments from the database.
     * @param assignmentID the course session's unique ID in the database
     */
    CourseAssignment(int assignmentID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get course assignment details (if course assignment exists)
        String sql = "SELECT * FROM courseAssignment WHERE assignmentID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = assignmentID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.assignmentID = rs.getInt("assignmentID");
            this.courseID = rs.getInt("courseID");
            this.name = rs.getString("assignmentName");
            this.dueDate = rs.getTimestamp("dueDate");
            this.priority = rs.getInt("priority");
            this.possibleMark = rs.getDouble("possibleMark");
            this.earnedMark = rs.getDouble("earnedMark");
            this.weighting = rs.getDouble("weighting");
            this.note = rs.getString("note");
            this.complete = rs.getBoolean("complete");
        } else{
            throw new NullPointerException("No CourseAssignment exists with the assignmentID " + assignmentID);
        }
    }

    public Integer getAssignmentID() {
        return this.assignmentID;
    }

    public void setCourseID(int courseID){
        this.courseID = courseID;
        this.recordSaved = false;
    }
    public Integer getCourseID(){
        return this.courseID;
    }

    public void setName(String name) {
        this.name = name;
        this.recordSaved = false;
    }
    public String getName() {
        return this.name;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
        this.recordSaved = false;
    }
    public Timestamp getDueDate() { return this.dueDate; }

    public void setPriority(int priority) {
        this.priority = priority;
        this.recordSaved = false;
    }
    public Integer getPriority() {
        return this.priority;
    }

    public void setPossibleMark(double possibleMark) {
        this.possibleMark = possibleMark;
        this.recordSaved = false;
    }
    public Double getPossibleMark() {
        return this.possibleMark;
    }

    public void setEarnedMark(double earnedMark) {
        this.earnedMark = earnedMark;
        this.recordSaved = false;
    }
    public Double getEarnedMark() { return this.earnedMark; }

    public void setWeighting(double weighting) {
        this.weighting = weighting;
        this.recordSaved = false;
    }
    public Double getWeighting() { return this.weighting; }

    public void setNote(String note) {
        this.note = note;
        this.recordSaved = false;
    }
    public String getNote() {
        return this.note;
    }

    public void setCompleted(boolean bool) {
        this.complete = bool;
        this.recordSaved = false;
    }
    public boolean getCompleted() { return this.complete;}

    /**
     * Save the course assignment's details to the database.
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the assignmentID thereafter), then cannot save
        if(this.recordExists && this.assignmentID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists) {
            sql = "INSERT INTO courseAssignment (courseID, name, dueDate, priority, possibleMark, earnedMark, weighting, note, complete) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE courseAssignment SET courseID = ?, name = ?, dueDate = ?, priority = ?, possibleMark = ?, earnedMark = ?, "
                    + "weighting = ?, note = ?, complete = ? WHERE assignmentID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[9];
            types = new int[9];
        }else{
            params = new Object[10];
            types = new int[10];
            params[9] = this.assignmentID;
            types[9] = Types.INTEGER;
        }
        params[0] = this.courseID;
        types[0] = Types.INTEGER;
        params[1] = this.name;
        types[1] = Types.VARCHAR;
        params[2] = this.dueDate;
        types[2] = Types.TIMESTAMP;
        params[3] = this.priority;
        types[3] = Types.INTEGER;
        params[4] = this.possibleMark;
        types[4] = Types.DOUBLE;
        params[5] = this.earnedMark;
        types[5] = Types.DOUBLE;
        params[6] = this.weighting;
        types[6] = Types.DOUBLE;
        params[7] = this.note;
        types[7] = Types.VARCHAR;
        params[8] = this.complete;
        types[8] = Types.BOOLEAN;
        // execute query
        if(Database.update(sql, params, types)){
            // get assignment ID
            sql = "SELECT assignmentID FROM courseAssignment WHERE courseID = ?, name = ?, dueDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.courseID;
            types[0] = Types.INTEGER;
            params[1] = this.name;
            types[1] = Types.VARCHAR;
            params[2] = this.dueDate;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the assignmentID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.assignmentID = rs.getInt("assignmentID");
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
     * Delete the assignment's details from the database.
     * @return true if successful, false otherwise.
     */
    private boolean deleteAssignment () {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM courseAssignment WHERE assignmentID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.assignmentID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            return true;
        } else {
            System.out.println("Failed to delete course assignment for courseAssignmentID: " + this.assignmentID + ".");
            return false;
        }
    }
}
