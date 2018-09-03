package com.stms.web;

import java.sql.*;
import java.util.Date;

/**
 * StudySessiom class for Student Time Management System
 * Used to create new and edit existing study sessions in the database.
 * @author Scott Hallauer, Jonathon Everatt and Jessica Bourn
 * @version 19/08/2018
 */

public class StudySession {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer sSessionID;
    private Integer assignmentID;
    private Integer coursesessionID;
    private Integer semesterID;
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean confirmed;
    private String note;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new study session records in the database.
     */
    public StudySession () {
        this.recordExists = false;
        this.recordSaved = false;
        this.assignmentID = null;
        this.coursesessionID = null;
    }

    /**
     * Parameterised constructor used to create and fetch any existing study sessions from the database.
     * @param sSessionID the study session's unique ID in the database
     */
    StudySession(int sSessionID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get study session details (if study session exists)
        String sql = "SELECT * FROM studySession WHERE sSessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = sSessionID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.sSessionID = rs.getInt("sSessionID");
            this.semesterID = rs.getInt("semesterID");
            this.startTime = rs.getTimestamp("startTime");
            this.endTime = rs.getTimestamp("endTime");
            this.confirmed = rs.getBoolean("confirmed");
            this.note = rs.getString("note");
            this.assignmentID = rs.getInt("assignmentID");
            this.coursesessionID = rs.getInt("courseSessionID");
        } else{
            throw new NullPointerException("No CourseSession exists with the sSessionID " + sSessionID);
        }
    }

    /**
     * Save the study session's details to the database.
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the PsessionID thereafter), then cannot save
        if (this.recordExists && this.sSessionID == null) {
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if (!this.recordExists) {
            sql = "INSERT INTO studySession (semesterID, courseSessionID, assignmentID, startTime, endTime, confirmed, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE studySession SET semesterID = ?, courseSessionID = ?, assignmentID = ?, startTime = ?, endTime = ?, confirmed = ?, note = ? WHERE sSessionID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if (!this.recordExists) {
            params = new Object[7];
            types = new int[7];
        } else {
            params = new Object[8];
            types = new int[8];
            params[7] = this.sSessionID;
            types[7] = Types.INTEGER;
        }
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        params[1] = this.coursesessionID;
        types[1] = Types.INTEGER;
        params[2] = this.assignmentID;
        types[2] = Types.INTEGER;
        params[3] = this.startTime;
        types[3] = Types.TIMESTAMP;
        params[4] = this.endTime;
        types[4] = Types.TIMESTAMP;
        params[5] = this.confirmed;
        types[5] = Types.BOOLEAN;
        params[6] = this.note;
        types[6] = Types.VARCHAR;


        // execute query
        if (Database.update(sql, params, types)) {
            // get study session ID
            sql = "SELECT sSessionID from studySession WHERE semesterID = ? AND startTime = ? AND endTime = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.semesterID;
            types[0] = Types.INTEGER;
            params[1] = this.startTime;
            types[1] = Types.TIMESTAMP;
            params[2] = this.endTime;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the courseID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.sSessionID = rs.getInt("sSessionID");
                }
            } catch (Exception e) {
            }
            this.recordExists = true;
            this.recordSaved = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete the course session's details from the database.
     * @return true if successful, false otherwise.
     */
    private boolean delete() {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM studySession WHERE sSessionID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.sSessionID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            return true;
        } else {
            System.out.println("Failed to delete course session for studySessionID: " + this.sSessionID + ".");
            return false;
        }
    }

    public Integer getStudySessionID() {
        return this.sSessionID;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
        this.recordSaved = false;
    }
    public Integer getSemesterID() {
        return this.semesterID;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        this.recordSaved = false;
    }
    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        this.recordSaved = false;
    }
    public Timestamp getEndTime () { return this.endTime; }

    public void setConfirmed(boolean bool) {
        this.confirmed = bool;
        this.recordSaved = false;
    }
    public boolean getConfirmed() { return this.confirmed;}

    public void setNote(String note) {
        this.note = note;
        this.recordSaved = false;
    }

    public void setCoursesessionID(int coursesessionID){
        this.coursesessionID = coursesessionID;
    }

    public int getCourseSessionID(){
        return  coursesessionID;
    }

    public void setAssignmentID(int AssignmentID){
        this.assignmentID = AssignmentID;
    }

    public int getAssignmentID(){
        return assignmentID;
    }

    public String getNote() {
        return this.note;
    }
}



