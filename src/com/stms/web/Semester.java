package com.stms.web;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private Course[] courses;
    private StudySession[] studySessions;

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
            this.startDate = rs.getDate("startDate").toLocalDate();
            this.endDate = rs.getDate("endDate").toLocalDate();
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
        ArrayList<Course> arr = new ArrayList<Course>();
        try {
            while(rs.next()){
                arr.add(new Course(rs.getInt("courseID")));
            }
        } catch (Exception e){
            System.out.println("Failed to load all courses for Semester (semesterID: " + this.semesterID + ").");
            e.printStackTrace();
        }
        this.courses = arr.toArray(new Course[0]);
    }

    /**
     * loads courses if they haven't been loaded yet and then returns the array of courses that are associated
     * with this semester
     *
     * @return an array of courses associated with this semesters
     */
    public Course[] getCourses () {
        this.loadCourses();
        if(this.courses == null){
            this.courses = new Course[0];
        }
        return this.courses;
    }

    /**
     * Loads all of the study sessions for the semester into an array stored as an attribute.
     */
    private void loadStudySessions(){
        // check if database is connected
        if(!Database.isConnected()) {
            return;
        }
        String sql = "SELECT * FROM studySession WHERE semesterID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        ArrayList<StudySession> arr = new ArrayList<StudySession>();
        try {
            while(rs.next()){
                arr.add(new StudySession(rs.getInt("sSessionID")));
            }
        } catch (Exception e){
            System.out.println("Failed to load all study sessions for Semester (semesterID: " + this.semesterID + ").");
            e.printStackTrace();
        }
        this.studySessions = arr.toArray(new StudySession[0]);
    }

    /**
     * Loads an all the study sessions associated with this semester into an array in the class is not already loaded
     * and then returns said array
     *
     * @return all the study sessions associated with this semester
     */
    public StudySession[] getStudySessions() {
        this.loadStudySessions();
        if(this.studySessions == null){
            this.studySessions = new StudySession[0];
        }
        return this.studySessions;
    }

    /**
     * Setters and getters for the class
     */
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

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.recordSaved = false;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.recordSaved = false;
    }

    /**
     * Save the semester's details to the database.
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
        types[2] = Types.DATE;
        params[3] = this.endDate;
        types[3] = Types.DATE;
        // execute query
        if(Database.update(sql, params, types)){
            // get semester ID
            sql = "SELECT semesterID FROM semester WHERE userID = ? AND startDate = ? AND endDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.userID;
            types[0] = Types.INTEGER;
            params[1] = this.startDate;
            types[1] = Types.DATE;
            params[2] = this.endDate;
            types[2] = Types.DATE;
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
    public boolean delete() {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM studySession WHERE semesterID = ?";
        Object[] params;
        int[] types;
        params = new Object[1];
        types = new int[1];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)) {
            System.out.println("Failed to delete all database entries for Semester (semesterID: " + this.semesterID + ").");
            return false;
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
                deletedCourse = rs.getInt("courseID");
                sql = "DELETE FROM courseSession WHERE courseID = ?";
                params = new Object[1];
                types = new int[1];
                params[0] = deletedCourse;
                types[0] = Types.INTEGER;
                if(!Database.update(sql, params, types)) {
                    System.out.println("Failed to delete all database entries for Semester (semesterID: " + this.semesterID + ").");
                    return false;
                }
                sql = "DELETE FROM courseAssignment WHERE courseID = ?";
                if(!Database.update(sql, params, types)) {
                    System.out.println("Failed to delete all database entries for Semester (semesterID: " + this.semesterID + ").");
                    return false;
                }
                sql = "DELETE FROM course WHERE courseID = ?";
                if(!Database.update(sql, params, types)) {
                    System.out.println("Failed to delete all database entries for Semester (semesterID: " + this.semesterID + ").");
                    return false;
                }
            }
        } catch (Exception e){
            System.out.println("Failed to load all courses for Semester (semesterID: " + this.semesterID + ") while deleting.");
            e.printStackTrace();
        }
        sql = "DELETE FROM semester WHERE semesterID = ?";
        params = new Object[1];
        types = new int[1];
        params[0] = this.semesterID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)) {
            System.out.println("Failed to delete all database entries for Semester (semesterID: " + this.semesterID + ").");
            return false;
        }
        return true;
    }

}
