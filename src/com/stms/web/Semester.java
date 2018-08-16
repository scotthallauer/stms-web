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

    /**
     * Constructs the Semester class and provides it with it's unqiue ID.
     * Fetches all the relevant data about the semster class from the database
     * using its unique ID and saves it in the correct format in the class
     * finally calls loadCourse to load all the necessary courses for the semester
     *
     *
     * @param semesterID unique value from the database
     */
	
    Semester(int semesterID) {
        DB = new Database();
        this.semesterID = semesterID;

        System.out.println("SemesterID ONLY constructor called. Databse connected");

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

        loadCourse();
        System.out.println("Courses loaded into semester. Semester object constructed");
    }

    Semester(int semesterID, String userID, String name, Date start, Date end){
        //THIS METHOD HAS NOT BEEN TESTED
        System.out.println(" Semester constructor called with all variable constructors");
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

	/**
	 * The course associated with the Semester in the database via the foreign semesterID key
     *  connection is set up and DB is filtered to get and load all  realated to the semester
	*/

    private void loadCourse(){
        System.out.println("Loadcourse called");
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
        System.out.println("Successful. All course loaded to the semester");
    }

    /**
     * Formats the Date to a string that can be inserted into sql
     *
     * @param date neeeded to insert inot a database
     * @return String format of date to inset into sql database
     */

    public String DateFormat(Date date){
        //Turns Date into a format readable by SQL
        String s = date.toString();
        s = s.substring(0,4) + s.substring(5,7) + s.substring(8,10);
        System.out.println(s);
        return s;
    }

    /**
     * @param password to save to the Database
     * Saves the information in the object to the corresponding table in the database
     */

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
