package com.stms.web;

import java.time.LocalTime;
import java.util.Date;
import java.sql.ResultSet;

public class CourseSession {

    private int cSessionID;
    private  int courseID;
    private String type;
    private String name;
    private Date startTime;
    private Date endTime;
    private String note;
    private String location;
    private String rrule;
    private int weighting;
    private double possibleMark;
    private double earnedMark;
    private int priority;

    // Various constructors

	// Constructor for CourseSession which fetches its own data from DB
	
     CourseSession(int ID) {
        Database DB = new Database();
        String sql = "SELECT * FROM coursesession WHERE sessionID = " + ID + ";";
        ResultSet rs = DB.query(sql);

        try {
            this.courseID = rs.getInt(2);
            this.name = rs.getString(3);
            this.type = rs.getString(4);
            this.startTime = rs.getTimestamp(5);
            this.endTime = rs.getTimestamp(6);
            this.location = rs.getString(7);
            this.rrule = rs.getString(8);
            this.note = rs.getString(9);
            System.out.print("Constructor completed off Session ID. Data gotten from DB");
        } catch (SQLException e){
            e.printStackTrace();
            System.out.print("Constructor fail coursesession");
        }
    }
	
	//Constructor for when class is loaded by course
	
    CourseSession() {

    }
	
	//Formats the Date into a format usable to insert into the sql database
	
	public String DateFormat(Date date){
        //Turns Date into a format readable by SQL
        String s = date.toString();
        s = s.substring(0,4) + s.substring(5,7) + s.substring(8,10);
        System.out.println(s);
        return s;
    }

	/*
	 * Saves the data of the class to the database. Used when creating a new
	 * coursesession from the application.
	*/
	
    public void saveToDB(){
        String sql = "INSERT INTO courseSession (courseID, sessionName, sessionType, startTime, endTime, location, rrule, note) ";
        sql = sql + "(" + courseID + ",'" + name + "','" + type + "'," + DateFormat(startTime) + "," + DateFormat(endTime) + ",'";
        sql = sql + location + "','" + rrule + "','" + note + "');";
        Database DB = new Database();
        DB.update(sql);
    }


    // getters and setters for all variables

    public void setcSessionID(int ID) {
        this.cSessionID = ID; }

    public int getcSessionID() {
        return cSessionID; }

    public void setType(String type) {
        this.type = type; }

    public String getType() {
        return type; }

    public void setName(String name) {
        this.name = name; }

    public void setCourseID(int ID){
        this.courseID = ID;
    }

    public String getName() {
        return name; }

    public void setStartTime(Date time) {
        this.startTime = time; }

    public Date getStartTime() {
        return startTime; }

    public void setEndTime(Date time) {
        this.endTime = time; }

    public Date getEndTime() {
        return endTime; }

    public void setLocation(String venue) {
        this.location = venue; }

    public String getLocation() {
        return location; }

    public void setNote(String notes) {
        this.note = notes; }

    public String getNote() {
        return note; }

    public void setRRule(String RRule) {
        this.rrule = RRule; }

    public String getRRule() {
        return rrule; }

    public void setWeighting(int weight) {
        this.weighting = weight; }

    public int getWeighting() {
        return weighting; }

    public void setPriority(int priority) {
        this.priority = priority; }

    public int getPriority() {
        return priority; }

    public void setPossibleMark(double possible) {
        this.possibleMark = possible; }

    public double getPossibleMark() {
        return possibleMark; }

    public void setEarnedMark(double earned) {
        this.earnedMark = earned; }

    public double getEarnedMark() {
        return earnedMark; }


}
