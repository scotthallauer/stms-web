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

    private Integer PsessionID;
    private Integer semesterID;
    private Timestamp startDate;
    private Timestamp endDate;
    private String note;
    //private boolean confirmed;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new study session records in the database.
     */
    public StudySession () {
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch any existing study sessions from the database.
     * @param PsessionID the study session's unique ID in the database
     */
    StudySession(int PsessionID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get study session details (if study session exists)
        String sql = "SELECT * FROM studySession WHERE PsessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = PsessionID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.PsessionID = rs.getInt("PsessionID");
            this.semesterID = rs.getInt("semesterID");
            this.startDate = rs.getTimestamp("startDate");
            this.endDate = rs.getTimestamp("endDate");
            this.note = rs.getString("note");
        } else{
            throw new NullPointerException("No CourseSession exists with the sessionID " + PsessionID);
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the sessionID thereafter), then cannot save
        if (this.recordExists && this.PsessionID == null) {
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if (!this.recordExists) {
            sql = "INSERT INTO studySession (semesterID, startDate, endDate, note) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE studySession SET semesterID = ?, startDate = ?, endDte = ?, note = ? WHERE PsessionID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if (!this.recordExists) {
            params = new Object[4];
            types = new int[4];
        } else {
            params = new Object[5];
            types = new int[5];
            params[4] = this.PsessionID;
            types[4] = Types.INTEGER;
        }
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        params[1] = this.startDate;
        types[1] = Types.TIMESTAMP;
        params[2] = this.endDate;
        types[2] = Types.TIMESTAMP;
        params[3] = this.note;
        types[3] = Types.VARCHAR;
        // execute query
        if (Database.update(sql, params, types)) {
            // get study session ID
            sql = "SELECT PsessionID from studySession WHERE semesterID = ?, startDate = ?, endDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.semesterID;
            types[0] = Types.INTEGER;
            params[1] = this.startDate;
            types[1] = Types.TIMESTAMP;
            params[2] = this.endDate;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the courseID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.PsessionID = rs.getInt("PsessionID");
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

    public void setSessionID(int PsessionID){
        this.PsessionID = PsessionID;
    }
    public Integer getSessionID() {
        return this.PsessionID;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }
    public Integer getSemesterID() {
        return this.semesterID;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return this.note;
    }
}
