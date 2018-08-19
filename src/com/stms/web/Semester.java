package com.stms.web;

import java.util.Date;
import java.sql.*;

/**
 * Semester class for Student Time Management System
 * Used to create new and edit existing semesters in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 16/08/2018
 */
public class Semester {

    // ATTRIBUTES //

    private int semesterID;
    private int userID;
    private String name;
    private Date startDate;
    private Date endDate;
    private Course[] courses;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new semester records in the database.
     */
    Semester(){}

    /**
     * Parameterised constructor used to create and fetch an existing semester from the database.
     * Loads all associated courses into memory.
     * @param semesterID the semester's unique ID in the database
     */
    Semester(int semesterID) throws Exception {
        // connect to database
        System.out.println("Semester constructor has been called.");
        Database db = new Database();
        if(db.isConnected()) {
            // query database to get semester details (if semester exists)
            ResultSet rs = db.query("SELECT * FROM semester WHERE semesterID = " + semesterID + ";");
            if (rs.first()) {
                this.semesterID = rs.getInt("semesterID");
                this.userID = rs.getInt("userID");
                this.name = rs.getString("semesterName");
                this.startDate = rs.getTimestamp("startDate");
                this.endDate = rs.getTimestamp("endDate");
            }else{
                throw new NullPointerException();
            }
            System.out.println("Successfully loaded Semester (semesterID: " + semesterID + ") from database.");
            this.loadCourses();
        }else{
            throw new SQLException();
        }
        System.out.println("Semester object has been constructed");
    }

    // METHODS //

    /**
     * Loads all of the courses for the semester into an array stored as an attribute.
     */
    private void loadCourses(){
        Database db = new Database();
        String sql = "SELECT * FROM course WHERE semesterID = " + this.semesterID + ";";
        ResultSet rs = db.query(sql);
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
            System.out.println("Failed to load all courses for semester (semesterID: " + this.semesterID + ")");
            e.printStackTrace();
        }
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }
    public int getSemesterID() {
        return this.semesterID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return this.userID;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getStartDate() {
        return this.startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Formats the Date into a format usable to insert into the MySQL database
     * @param date the Date value to be inserted as a field value into the database
     * @return the String format of the Date to insert into MySQL database
     */
    String DateFormat(Date date){
        // Turns Date into a format readable by SQL
        String s = date.toString();
        s = s.substring(0,4) + s.substring(5,7) + s.substring(8,10);
        System.out.println(s);
        return s;
    }

    /**
     * Save the semester's details to the database.
     * @return true if successful, false otherwise.
     */
    private boolean saveToDB(){
        Database db = new Database();
        if(db.isConnected()) {
            String sql = "INSERT INTO semester (userID,semesterName,startDate,endDate) " +
                    "VALUES (" + this.userID + ",'" + this.name + "'," + DateFormat(this.startDate) + "," + DateFormat(this.endDate) + ");";
            db.update(sql);
            return true;
        }else{
            return false;
        }
    }

    // Methods still to be implemented
    /*
    public void setCourses(Course[] crs) {
        this.courses = crs; }

    public Course[] getCourses() {
        return courses; }

    public void addCourse (Course course) {
    }

    public StudySession[] getStudySession() {
    };
    */
}
