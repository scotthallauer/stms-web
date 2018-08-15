package com.stms.web;

import java.sql.*;

public class Course {

    private int courseID;
    private int semesterID;
    private CourseSession[] sessions;
    private String name;
    private String code;

    // Various constructors

    Course() { }

    Course(int courseID) {
        this.courseID = courseID;
        loadCourseSessions();
    }
	
	/*
	 * The loadCourseSessions method is used to load all of the course sessions for the 
	 * course into an array in the course class. So that they can be accessed off a 
	 * central location.
	*/

    private void loadCourseSessions(){
        Database DB = new Database();
        String sql = "SELECT * FROM courseSession WHERE courseID = '" + courseID + "';";
        ResultSet rs = DB.query(sql);

        try {
            if(rs.last()){
                sessions = new CourseSession[rs.getRow()];
                rs.first();
            }
            Boolean flag = false;
            int count = 0;
            while (rs.next()){
                if (flag == false){
                    rs.first();
                    flag = true;
                }

                sessions[count] = new CourseSession(courseID);
                sessions[count].setcSessionID(rs.getInt(1));
                //sessions[count].setCourseID((courseID));
                sessions[count].setName(rs.getString(3));
                sessions[count].setType(rs.getString(4));
                sessions[count].setStartTime(rs.getTimestamp(5));
                sessions[count].setEndTime(rs.getTimestamp(6));
                sessions[count].setLocation(rs.getString(7));
                sessions[count].setRRule(rs.getString(8));
                sessions[count].setNote(rs.getString(9));
                System.out.println(sessions[count].getName());
                count++;
                System.out.println("course session " + count + "loaded" );

            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Fail in try-catch loadCourseSessions");
        }
    }
	/*
	 * Save the data of the class to the data. Used in create methods of the class
	*/
	private boolean saveToDB(String password){
        String sql = "INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) \n" +
                "VALUES (" + semesterID + ",'" + name + "','" + code + "'," + semesterID + ",);";
        Database DB = new Database();
        DB.update(sql);
        return true;
    }

    /*public void addSession (CourseSession session) {

    }

    public void addAssignment (Assignment assignment) {

    }

    public CourseSession[] getSessions () {

    }

    public Assignment[] getAssignments() {

    }

    public boolean savetoDB() {

    }*/

    // getters and setters for all variables

    public void setSemesterID(int semesterID){
        this.semesterID = semesterID;
    }

    public void setCourseID(int ID) {
        this.courseID = ID; }

    public int getCourseID() {
        return courseID; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setCode(String code) {
        this.code = code; }

    public String getCode() {
        return code; }

}