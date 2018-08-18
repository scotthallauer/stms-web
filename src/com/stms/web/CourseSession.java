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
    private String note;
    private String location;
    private String rrule;
    private Double possibleMark;
    private Double earnedMark;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course session records in the database.
     */
    public CourseSession(){}

    /**
     * Parameterised constructor used to create and fetch an existing course sessions from the database.
     * @param sessionID the course session's unique ID in the database
     */
     CourseSession(int sessionID) throws Exception {
         // connect to database
         System.out.println("CourseSession constructor has been called.");
         Database db = new Database();
         if(db.isConnected()) {
             // query database to get course session details (if course session exists)
             ResultSet rs = db.query("SELECT * FROM courseSession WHERE sessionID = " + sessionID + ";");
             if (rs.first()) {
                 this.sessionID = rs.getInt("sessionID");
                 this.courseID = rs.getInt("courseID");
                 this.name = rs.getString("sessionName");
                 this.type = rs.getString("sessionType");
                 this.startTime = rs.getTimestamp("startTime");
                 this.endTime = rs.getTimestamp("endTime");
                 this.location = rs.getString("location");
                 this.rrule = rs.getString("rrule");
                 this.note = rs.getString("note");
             }else{
                 throw new NullPointerException();
             }
             System.out.println("Successfully loaded CourseSession (sessionID: " + sessionID + ") from database.");
         }else{
             throw new SQLException();
         }
         System.out.println("CourseSession object has been constructed");
     }

     public void setSessionID(int sessionID){
         this.sessionID = sessionID;
     }

     public int getSessionID() {
         return this.sessionID;
     }

     public void setCourseID(int courseID){
         this.courseID = courseID;
     }

     public int getCourseID(){
         return this.courseID;
     }

     public void setType(String type) {
         this.type = type;
     }

     public String getType() {
         return this.type;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getName() {
         return this.name;
     }

     public void setStartTime(Date startTime) {
         this.startTime = startTime;
     }

     public Date getStartTime() {
         return this.startTime;
     }

     public void setEndTime(Date endTime) {
         this.endTime = endTime;
     }

     public Date getEndTime() {
         return this.endTime;
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

     public void setRRule(String rrule) {
        this.rrule = rrule;
     }

     public String getRRule() {
         return this.rrule;
     }

     public void setWeighting(int weighting) {
         this.weighting = weighting;
     }

     public int getWeighting() {
         return this.weighting;
     }

     public void setPriority(int priority) {
         this.priority = priority;
     }

     public int getPriority() {
         return this.priority;
     }

     public void setPossibleMark(double possibleMark) {
         this.possibleMark = possibleMark;
     }

     public double getPossibleMark() {
         return this.possibleMark;
     }

     public void setEarnedMark(double earnedMark) {
         this.earnedMark = earnedMark;
     }

     public double getEarnedMark() {
         return this.earnedMark;
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
     * Save the course session's details to the database.
     * @return true if successful, false otherwise.
     */
    public boolean saveToDB(){
        Database db = new Database();
        if(db.isConnected()) {
            String sql = "INSERT INTO courseSession (courseID, sessionName, sessionType, startTime, endTime, location, rrule, note) " +
                    "VALUES (" + this.courseID + ",'" + this.name + "','" + this.type + "'," + DateFormat(this.startTime) + "," + DateFormat(this.endTime) + ",'" +
                    this.location + "','" + this.rrule + "','" + this.note + "');";
            db.update(sql);
            return true;
        }else{
            return false;
        }
    }

}
