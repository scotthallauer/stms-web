package com.stms.web;

import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;

public class Semester {

    private int semesterID;
    private String userID;
    private String name;
    private Date startDate;
    private Date endDate;
    private Database DB;
    private Course[] courses;

    // Various constructors

    Semester() { }
	
	/*
	 * Constructs the Semester class and provides it will it's unqiue ID.
	 * Fetches all the relevant data about the semster class from the database 
	 * using its unique ID and saves it in the correct format in the class
	 * finally calls loadCourse to load all the necessary courses for the semester
	*/
	
    Semester(int semesterID) {
        DB = new Database();
        this.semesterID = semesterID;

        System.out.println("SemesterID ONLY constructor called");

        String sql = "SELECT * FROM semester WHERE semesterID = '" + semesterID + "';";
        ResultSet rs = DB.query(sql);
        try {
            if (rs.next()){
                try{
                    this.semesterID = rs.getInt(1);
                    this.userID = rs.getString(2);
                    name = rs.getString(3);
                    startDate = rs.getTimestamp(4);
                    endDate = rs.getTimestamp(5);

                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        } catch
        (SQLException e){
            e.printStackTrace();
            System.out.println("Failed SEMESTERID ONLY CONSTRUCTOR");
        }
        System.out.println("semester ID: " + semesterID + " name : " + name);
        System.out.println("calling load courses");
        loadCourse();
        System.out.println("Courses loaded into semester");
    }

    Semester(int semesterID, String userID, String name, Date start, Date end){
        //THIS METHOD HAS NOT BEEN TESTED
        DB = new Database();
        this.semesterID = semesterID;
        this.userID = userID;
        this.name = name;
        startDate = start;
        endDate = end;

        System.out.println("Semester object created");
        //This method would be used to create the Semester object
        //gen unique ID method
        String query = "INSERT INTO semester (semesterID, userID, semesterName, startDate, endDate)" +
                " VALUES (" + semesterID + "," + userID + "," + name + "," + start + "," + end + ");";
    }
	
	/*
	 * Loads all the courses into the semester class and stores them in an array
	 * said courses are constructed and filled with data using set methods
	*/

    private void loadCourse(){
        String sql = "SELECT * FROM course WHERE semesterID = '" + semesterID + "';";
        ResultSet rs = DB.query(sql);
        //Course course;
        try{
            if(rs.last()){
                int count = rs.getRow();
                System.out.println(rs.getRow() + " NUMBER OF RS ROWS");
                rs.first();
                courses = new Course[count];
            }

            Boolean flag = false;
            while (rs.next()){
                if (flag == false){
                    rs.first();
                    flag = true;
                }
                int count = 0;
                courses[count] = new Course(rs.getInt(1));
                System.out.println("Course ID is " + courses[count].getCourseID());

                courses[count].setSemesterID(rs.getInt(2));
                courses[count].setName(rs.getString(3));
                courses[count].setCode(rs.getString(4));
                System.out.println("coursename: " + courses[count].getName());
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to load data into the Course class");
        }
    }
	
	//Formats Date into a format used to insert into sql

    public String DateFormat(Date date){
        //Turns Date into a format readable by SQL
        String s = date.toString();
        s = s.substring(0,4) + s.substring(5,7) + s.substring(8,10);
        System.out.println(s);
        return s;
    }
	
	//Saves the data from the class into the DataBase

    private void saveToDB(String password){
        String sql = "INSERT INTO user (userID, semesterName, startDate, endDate) \n" +
                "VALUES (" + userID + ", '" + name + "' ," + DateFormat(startDate) + "," + DateFormat(endDate) + ");";
        DB.update(sql);

    }

    // getters and setters for all variables

    /*public void setSemesterID(int ID) {
        this.semesterID = ID; }

    public int getSemesterID() {
        return semesterID; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setStartDate(Date date) {
        this.startDate = date; }

    public Date getStartDate() {
        return startDate; }

    public void setEndDate(Date date) {
        this.endDate = date; }

    public Date getEndDate() {
        return endDate; }

    public void setDB(Database db) {
        this.DB = db; }

    public Database getDB() {
        return DB; }

    public void setCourses(Course[] crs) {
        this.courses = crs; }

    public Course[] getCourses() {
        return courses; }


    public void addCourse (Course course) {

    }

    public StudySession[] getStudySession() {

    };*/
}
