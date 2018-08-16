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

    /**
     * Course class created through semester object and values set with set methods
     * setst the ID and then loads the sessions related to the class
     *
     * @param courseID unique ID from DB
     */
    Course(int courseID) {
        this.courseID = courseID;
        loadCourseSessions();
    }
	
	/**
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
                System.out.println(sessions[count].getName());
                count++;
                System.out.println("course session " + count + "loaded" );

            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Fail in try-catch loadCourseSessions");
        }
    }

    /**
     * Save the data of the class to the data. Used in create methods of the class
     *
     * @return true if successful and false if failed to save
     */
	private boolean saveToDB(){
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

    /**
     * Setter and getter methods for the class
     */

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
