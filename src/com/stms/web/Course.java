package com.stms.web;

import java.sql.*;

/**
 * Course class for Student Time Management System
 * Used to manage associated CourseSession and CourseAssignment objects, and to determine overall course grade.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 16/08/2018
 */
public class Course {

    // ATTRIBUTES //

    private int courseID;
    private int semesterID;
    private String name;
    private String code;
    private CourseSession[] sessions;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course records in the database.
     */
    public Course(){}

    /**
     * Parameterised constructor used to create and fetch an existing course from the database.
     * Loads all associated course sessions into memory.
     * @param courseID the course's unique ID in the database
     */
    Course(int courseID) throws Exception {
        // connect to database
        System.out.println("Course constructor has been called.");
        Database db = new Database();
        if(db.isConnected()) {
            // query database to get course details (if course exists)
            ResultSet rs = db.query("SELECT * FROM course WHERE courseID = " + courseID + ";");
            if (rs.first()) {
                this.courseID = rs.getInt("courseID");
                this.semesterID = rs.getInt("semsterID");
                this.name = rs.getString("courseName");
                this.code = rs.getString("courseCode");
            }else{
                throw new NullPointerException();
            }
            System.out.println("Successfully loaded Course (courseID: " + courseID + ") from database.");
            this.loadCourseSessions();
        }else{
            throw new SQLException();
        }
        System.out.println("Course object has been constructed");
    }

    // METHODS //

    /**
     * Loads all of the course sessions for the course into an array stored as an attribute.
     */
    private void loadCourseSessions(){
        Database db = new Database();
        String sql = "SELECT * FROM courseSession WHERE courseID = " + this.courseID + ";";
        ResultSet rs = db.query(sql);
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
            System.out.println("Failed to load all course sessions for course (courseID: " + this.courseID + ")");
            e.printStackTrace();
        }
    }

    public int getSemesterID(){
        return this.semesterID;
    }

    public void setSemesterID(int semesterID){
        this.semesterID = semesterID;
    }

    public int getCourseID() {
        return this.courseID;
    }

    public void setCourseID(int ID) {
        this.courseID = ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Save the course's details to the database.
     * @return true if successful, false otherwise.
     */
	private boolean saveToDB(){
        Database db = new Database();
        if(db.isConnected()) {
            String sql = "INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) " +
                    "VALUES (" + this.semesterID + ",'" + this.name + "','" + this.code + "'," + this.semesterID + ");";
            db.update(sql);
            return true;
        }else{
            return false;
        }
    }

    // Methods still to be implemented
    /*
    public void addSession (CourseSession session) {
    }

    public void addAssignment (Assignment assignment) {
    }

    public CourseSession[] getSessions () {
    }

    public Assignment[] getAssignments() {
    }
    */

}
