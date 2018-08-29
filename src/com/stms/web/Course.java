package com.stms.web;

import java.sql.*;

/**
 * Course class for Student Time Management System
 * Used to manage associated CourseSession and CourseAssignment objects, and to determine overall course grade.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 18/08/2018
 */
public class Course {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer courseID;
    private Integer semesterID1;
    private Integer semesterID2;
    private String name;
    private String code;
    private String colour;
    private CourseSession[] sessions;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course records in the database.
     */
    public Course(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch an existing course from the database.
     * Loads all associated course sessions into memory.
     * @param courseID the course's unique ID in the database
     */
    public Course(int courseID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get course details (if course exists)
        String sql = "SELECT * FROM course WHERE courseID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = courseID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.courseID = rs.getInt("courseID");
            this.semesterID1 = rs.getInt("semesterID1");
            this.semesterID2 = rs.getInt("semesterID2");
            if(rs.wasNull()) this.semesterID2 = null;
            this.name = rs.getString("courseName");
            this.code = rs.getString("courseCode");
            if(rs.wasNull()) this.code = null;
            this.colour = rs.getString("colour");
            if(rs.wasNull()) this.colour = null;
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No Course exists with the courseID " + courseID);
        }
    }

    // METHODS //

    /**
     * Loads all or only graded course sessions for the course into an array stored as an attribute.
     */
    private void loadCourseSessions(boolean graded){
        // check if database is connected
        if(!Database.isConnected()) {
            return;
        }
        String sql = "SELECT * FROM courseSession WHERE courseID = ?";
        if(graded){
            sql = "SELECT * FROM courseSession WHERE courseID = ? AND (priority IS NOT NULL OR weighting IS NOT NULL)";
        }
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.courseID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        try {
            // set length of array
            if(rs.last()){
                this.sessions = new CourseSession[rs.getRow()];
            }
            int count = 0;
            if(rs.first()){
                do{
                    this.sessions[count] = new CourseSession(rs.getInt("sessionID"));
                    count++;
                }while(rs.next());
            }
        } catch (Exception e){
            System.out.println("Failed to load all course sessions for Course (courseID: " + this.courseID + ").");
            e.printStackTrace();
        }
    }

    public CourseSession[] getSessions () {
        this.loadCourseSessions(false);
        if(this.sessions == null){
            this.sessions = new CourseSession[0];
        }
        return this.sessions;
    }

    public CourseSession[] getGradedSessions(){
        this.loadCourseSessions(true);
        if(this.sessions == null){
            this.sessions = new CourseSession[0];
        }
        return this.sessions;
    }

    public Integer getCourseID() {
        return this.courseID;
    }

    public Integer getSemesterID1(){
        return this.semesterID1;
    }

    public void setSemesterID1(int semesterID1){
        this.semesterID1 = semesterID1;
        this.recordSaved = false;
    }

    public Integer getSemesterID2(){
        return this.semesterID2;
    }

    public void setSemesterID2(int semesterID2){
        this.semesterID2 = semesterID2;
        this.recordSaved = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.recordSaved = false;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
        this.recordSaved = false;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        this.recordSaved = false;
    }

    /**
     * Save the course's details to the database.
     * @return true if successful, false otherwise.
     */
	private boolean save(){
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // don't need to save to database, there have been no changes
        if(this.recordSaved){
            return true;
        }
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the courseID thereafter), then cannot save
        if(this.recordExists && this.courseID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists){
            sql = "INSERT INTO course (semesterID1, semesterID2, courseName, courseCode, colour) VALUES (?, ?, ?, ?, ?)";
        }else{
            sql = "UPDATE course SET semesterID1 = ?, semesterID2 = ?, courseName = ?, courseCode = ?, colour = ? WHERE courseID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[5];
            types = new int[5];
        }else{
            params = new Object[6];
            types = new int[6];
            params[5] = this.courseID;
            types[5] = Types.INTEGER;
        }
        params[0] = this.semesterID1;
        types[0] = Types.INTEGER;
        params[1] = this.semesterID2;
        types[1] = Types.INTEGER;
        params[2] = this.name;
        types[2] = Types.VARCHAR;
        params[3] = this.code;
        types[3] = Types.VARCHAR;
        params[4] = this.colour;
        types[4] = Types.VARCHAR;
        // execute query
        if(Database.update(sql, params, types)){
            // get course ID
            sql = "SELECT courseID FROM course WHERE semesterID1 = ? AND semesterID2 = ? AND courseName = ? AND courseCode = ?";
            params = new Object[4];
            types = new int[4];
            params[0] = this.semesterID1;
            types[0] = Types.INTEGER;
            params[1] = this.semesterID2;
            types[1] = Types.INTEGER;
            params[2] = this.name;
            types[2] = Types.VARCHAR;
            params[3] = this.code;
            types[3] = Types.VARCHAR;
            ResultSet rs = Database.query(sql, params, types); // if fetching the courseID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.courseID = rs.getInt("courseID");
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
     * Delete the course's details from the database, along with all sessions and assignments related to it.
     * @return true if successful, false otherwise.
     */
    private boolean deleteCourse () {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // the count will be used to make sure all three classes of objects related to the course are deleted from the database
        int count = 0;

        String sql = "DELETE FROM courseSession WHERE courseID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.courseID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            count++;
        }
        sql = "DELETE FROM courseAssignment WHERE courseID = ?";
        if(Database.update(sql, params, types)) {
            count++;
        }
        sql = "DELETE FROM course WHERE courseID = ?";
        if(Database.update(sql, params, types)) {
            count++;
        }
        if (count == 3) {
            return true;
        } else {
            System.out.println("Failed to delete all database entries for Course (courseID: " + this.courseID + ").");
            return false;
        }
    }

    // Methods still to be implemented
    /*
    public void addSession (CourseSession session) {
    }

    public void addAssignment (Assignment assignment) {
    }

    public Assignment[] getAssignments() {
    }
    */

}
