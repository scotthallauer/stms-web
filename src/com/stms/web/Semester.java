package com.stms.web;

import java.util.Date;
import java.sql.*;

/**
 * Semester class for Student Time Management System
 * Used to create new and edit existing semesters in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 19/08/2018
 */
public class Semester {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer semesterID;
    private Integer userID;
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private Course[] courses;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new semester records in the database.
     */
    public Semester(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch an existing semester from the database.
     * Loads all associated courses into memory.
     * @param semesterID the semester's unique ID in the database
     */
    public Semester(int semesterID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get semester details (if semester exists)
        String sql = "SELECT * FROM semester WHERE semesterID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = semesterID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.semesterID = rs.getInt("semesterID");
            this.userID = rs.getInt("userID");
            this.name = rs.getString("semesterName");
            this.startDate = rs.getTimestamp("startDate");
            this.endDate = rs.getTimestamp("endDate");
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No Semester exists with the semesterID " + semesterID);
        }
    }

    // METHODS //

    /**
     * Loads all of the courses for the semester into an array stored as an attribute.
     */
    private void loadCourses(){
        // check if database is connected
        if(!Database.isConnected()) {
            return;
        }
        String sql = "SELECT * FROM course WHERE semesterID1 = ? OR semesterID2 = ?";
        Object[] params = new Object[2];
        int[] types = new int[2];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        params[1] = this.semesterID;
        types[1] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        try {
            // set length of array
            if(rs.last()){
                this.courses = new Course[rs.getRow()];
            }
            int count = 0;
            if(rs.first()){
                do{
                    this.courses[count] = new Course(rs.getInt("courseID"));
                    count++;
                }while(rs.next());
            }
        } catch (Exception e){
            System.out.println("Failed to load all courses for Semester (semesterID: " + this.semesterID + ").");
            e.printStackTrace();
        }
    }

    public Course[] getCourses () {
        this.loadCourses();
        if(this.courses == null){
            this.courses = new Course[0];
        }
        return this.courses;
    }

    public int getSemesterID() {
        return this.semesterID;
    }

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

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        this.recordSaved = false;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        this.recordSaved = false;
    }

    /**
     * Save the semester's details to the database.
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the semesterID thereafter), then cannot save
        if(this.recordExists && this.semesterID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists){
            sql = "INSERT INTO semester (userID, semesterName, startDate, endDate) VALUES (?, ?, ?, ?)";
        }else{
            sql = "UPDATE semester SET userID = ?, semesterName = ?, startDate = ?, endDate = ? WHERE semesterID = ?";
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
            params[4] = this.semesterID;
            types[4] = Types.INTEGER;
        }
        params[0] = this.userID;
        types[0] = Types.INTEGER;
        params[1] = this.name;
        types[1] = Types.VARCHAR;
        params[2] = this.startDate;
        types[2] = Types.TIMESTAMP;
        params[3] = this.endDate;
        types[3] = Types.TIMESTAMP;
        // execute query
        if(Database.update(sql, params, types)){
            // get semester ID
            sql = "SELECT semesterID FROM semester WHERE userID = ? AND startDate = ? AND endDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.userID;
            types[0] = Types.INTEGER;
            params[1] = this.startDate;
            types[1] = Types.TIMESTAMP;
            params[2] = this.endDate;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the semesterID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.semesterID = rs.getInt("semesterID");
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
     * Delete the semester's details from the database, along with all information related to a semester,
     * including courses, courseAssignments, courseSessions and studySessions.
     * @return true if successful, false otherwise.
     */
    public boolean deleteSemester () {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // the count will be used to make sure all five classes of objects are deleted from the database
        int count = 0;
        String sql = "DELETE FROM semester WHERE semesterID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            count++;
        }
        sql = "DELETE FROM studySession WHERE semesterID = ?";
        params = new Object[1];
        types = new int[1];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            count++;
        }
        sql = "SELECT courseID FROM course WHERE semesterID1 = ? OR semesterID2 = ?";
        params = new Object[2];
        types = new int[2];
        params[0] = semesterID;
        types[0] = Types.INTEGER;
        params[1] = this.semesterID;
        types[1] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        try {
            int deletedCourse;
            while (rs.next()) {
                sql = "DELETE FROM courseSession WHERE courseID = ?";
                deletedCourse = rs.getInt(1);
                params = new Object[1];
                types = new int[1];
                params[0] = deletedCourse;
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
            }
        } catch (Exception e){
            System.out.println("Failed to load all courses for Semester (semesterID: " + this.semesterID + ") while deleting.");
            e.printStackTrace();
        }
        if (count == 5) {
            return true;
        } else {
            System.out.println("Failed to delete all database entries for semesterID: " + this.semesterID + ".");
            return false;
        }
    }


    // Methods still to be implemented
    /*
    public void setCourses(Course[] crs) {
        this.courses = crs; }


    public void addCourse (Course course) {
    }

    public StudySession[] getStudySession() {
    };
    */
}
