package com.stms.web;

import java.util.Date;
import java.sql.*;

/**
 * CourseSession class for Student Time Management System
 * Used to create new and edit existing course sessions in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 18/08/2018
 */
public class CourseSession {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer sessionID;
    private Integer sessionPID;
    private Integer courseID;
    private String name;
    private String type;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer length;
    private String recType;
    private String location;
    private String note;
    private Double possibleMark;
    private Double earnedMark;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course session records in the database.
     */
    public CourseSession(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch any existing course sessions from the database.
     * @param sessionID the course session's unique ID in the database
     */
    CourseSession(int sessionID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get course session details (if course session exists)
        String sql = "SELECT * FROM courseSession WHERE sessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = sessionID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.sessionID = rs.getInt("sessionID");
            this.sessionPID = rs.getInt("sessionPID");
            this.courseID = rs.getInt("courseID");
            this.name = rs.getString("sessionName");
            this.type = rs.getString("sessionType");
            this.startDate = rs.getTimestamp("startDate");
            this.endDate = rs.getTimestamp("endDate");
            this.length = rs.getInt("length");
            this.recType = rs.getString("recType");
            this.location = rs.getString("location");
            this.note = rs.getString("note");
            this.possibleMark = rs.getDouble("possibleMark");
            this.earnedMark = rs.getDouble("earnedMark");
        }else{
            throw new NullPointerException("No CourseSession exists with the sessionID " + sessionID);
        }
    }

    public void setSessionID(int sessionID){ this.sessionID = sessionID; }
    public Integer getSessionID() {
        return this.sessionID;
    }

    public void setSessionPID(int sessionPID){
        this.sessionPID = sessionPID;
    }
    public Integer getSessionPID() {
        return this.sessionPID;
    }

    public void setCourseID(int courseID){
        this.courseID = courseID;
    }
    public Integer getCourseID(){
        return this.courseID;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }
    public String getType() {
        return this.type;
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

    public void setLength(int length) {
        this.length = length;
    }
    public Integer getLength() {
        return this.length;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }
    public String getRecType() {
        return this.recType;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return this.location;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return this.note;
    }

    public void setPossibleMark(double possibleMark) {
        this.possibleMark = possibleMark;
    }
    public Double getPossibleMark() {
        return this.possibleMark;
    }

    public void setEarnedMark(double earnedMark) { this.earnedMark = earnedMark; }
    public Double getEarnedMark() { return this.earnedMark; }

    /**
     * Save the course session's details to the database.
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the sessionID thereafter), then cannot save
        if(this.recordExists && this.sessionID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists){
            sql = "INSERT INTO courseSession (sessionPID, courseID, sessionName, sessionType, startDate, endDate, length, recType, location, note, possibleMark, earnedMark) "
                  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }else{
            sql = "UPDATE courseSession SET sessionPID = ?, courseID = ?, sessionName = ?, sessionType = ?, startDate = ?, endDate = ?, length = ?, "
                  + " recType = ?, location = ?, note = ?, possibleMark = ?, earnedMark = ? WHERE sessionID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[12];
            types = new int[12];
        }else{
            params = new Object[13];
            types = new int[13];
            params[12] = this.sessionID;
            types[12] = Types.INTEGER;
        }
        params[0] = this.sessionPID;
        types[0] = Types.INTEGER;
        params[1] = this.courseID;
        types[1] = Types.INTEGER;
        params[2] = this.name;
        types[2] = Types.VARCHAR;
        params[3] = this.type;
        types[3] = Types.VARCHAR;
        params[4] = this.startDate;
        types[4] = Types.TIMESTAMP;
        params[5] = this.endDate;
        types[5] = Types.TIMESTAMP;
        params[6] = this.length;
        types[6] = Types.INTEGER;
        params[7] = this.recType;
        types[7] = Types.VARCHAR;
        params[8] = this.location;
        types[8] = Types.VARCHAR;
        params[9] = this.note;
        types[9] = Types.VARCHAR;
        params[10] = this.possibleMark;
        types[10] = Types.DOUBLE;
        params[11] = this.earnedMark;
        types[11] = Types.DOUBLE;
        // execute query
        if(Database.update(sql, params, types)){
            // get session ID
            sql = "SELECT sessionID FROM courseSession WHERE courseID = ?, sessionName = ?, sessionType = ?, startDate = ?, endDate = ?, recType = ?";
            params = new Object[6];
            types = new int[6];
            params[0] = this.courseID;
            types[0] = Types.INTEGER;
            params[1] = this.name;
            types[1] = Types.VARCHAR;
            params[2] = this.type;
            types[2] = Types.VARCHAR;
            params[3] = this.startDate;
            types[3] = Types.TIMESTAMP;
            params[4] = this.endDate;
            types[4] = Types.TIMESTAMP;
            params[5] = this.recType;
            types[5] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the sessionID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.sessionID = rs.getInt("sessionID");
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
