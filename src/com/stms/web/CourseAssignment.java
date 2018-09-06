package com.stms.web;

import java.util.Date;
import java.sql.*;

/**
 * CourseAssignment class for Student Time Management System
 * Used to create new and edit existing course assignments in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 19/08/2018
 */
public class CourseAssignment {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;

    private Integer assignmentID;
    private Integer courseID;
    private String description;
    private Timestamp dueDate;
    private Integer priority;
    private Double weighting;
    private Integer studyHours;
    private Boolean complete;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course assignment records in the database.
     */
    public CourseAssignment(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch any existing course assignments from the database.
     * @param assignmentID the course session's unique ID in the database
     */
    public CourseAssignment(int assignmentID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get course assignment details (if course assignment exists)
        String sql = "SELECT * FROM courseAssignment WHERE assignmentID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = assignmentID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.assignmentID = rs.getInt("assignmentID");
            this.courseID = rs.getInt("courseID");
            this.description = rs.getString("description");
            this.dueDate = rs.getTimestamp("dueDate");
            this.priority = rs.getInt("priority");
            if(rs.wasNull()){
                this.priority = null;
            }
            this.weighting = rs.getDouble("weighting");
            if(rs.wasNull()){
                this.weighting = null;
            }
            this.studyHours = rs.getInt("studyHours");
            if(rs.wasNull()){
                this.studyHours = null;
            }
            this.complete = rs.getBoolean("complete");
            this.recordExists = true;
            this.recordSaved = true;
        } else{
            throw new NullPointerException("No CourseAssignment exists with the assignmentID " + assignmentID);
        }
    }

    public Integer getAssignmentID() {
        return this.assignmentID;
    }

    public void setCourseID(int courseID){
        this.courseID = courseID;
        this.recordSaved = false;
    }
    public Integer getCourseID(){
        return this.courseID;
    }

    public void setDescription(String description) {
        this.description = description;
        this.recordSaved = false;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
        this.recordSaved = false;
    }
    public Timestamp getDueDate() { return this.dueDate; }

    public void setPriority(Integer priority){
        if(priority == null || (priority >= 1 && priority <= 3)){
            this.priority = priority;
            this.recordSaved = false;
        }
    }

    public Integer getPriority(){
        return this.priority;
    }

    public void setWeighting(Double weighting){
        if(weighting == null || (weighting > 0.0 && weighting <= 100.0)){
            this.weighting = weighting;
            this.recordSaved = false;
        }
    }

    public Double getWeighting(){
        if(this.weighting == null){
            return 0.0;
        }else {
            return this.weighting;
        }
    }

    public void setStudyHours(Integer studyHours){
        if(studyHours == null || studyHours > 0){
            this.studyHours = studyHours;
            this.recordSaved = false;
        }
    }

    public Integer getStudyHours(){
        if(this.studyHours == null){
            return 0;
        }else {
            return this.studyHours;
        }
    }

    public boolean isGraded(){
        return (this.priority != null);
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
        this.recordSaved = false;
    }
    public boolean isComplete() { return this.complete;}

    public boolean scheduleStudySessions(){

        // delete old study sessions associated with this course assignment
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM studySession WHERE assignmentID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.assignmentID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)){
            return false;
        }

        if(this.isGraded()){

            // schedule new study sessions
            try {
                int userID = new Semester(new Course(this.courseID).getSemesterID1()).getUserID();
                Scheduler scheduler = new Scheduler(userID);
                int unscheduledHours = this.studyHours;
                for(int i = 0 ; i < 5 && unscheduledHours > 0 ; i++) { // give the scheduling algorithm 5 chances to schedule all required hours
                    unscheduledHours = scheduler.generateSessions(unscheduledHours, this.dueDate.toLocalDateTime(), "assignment", this.assignmentID);
                }
                if(unscheduledHours > 0){
                    return false;
                }else{
                    return true;
                }
            }catch(Exception e){
                return false;
            }
        }
        return false;
    }

    /**
     * Save the course assignment's details to the database.
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
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the assignmentID thereafter), then cannot save
        if(this.recordExists && this.assignmentID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists) {
            sql = "INSERT INTO courseAssignment (courseID, description, dueDate, priority, weighting, studyHours, complete) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE courseAssignment SET courseID = ?, description = ?, dueDate = ?, priority = ?, "
                    + "weighting = ?, studyHours = ?, complete = ? WHERE assignmentID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[7];
            types = new int[7];
        }else{
            params = new Object[8];
            types = new int[8];
            params[7] = this.assignmentID;
            types[7] = Types.INTEGER;
        }
        params[0] = this.courseID;
        types[0] = Types.INTEGER;
        params[1] = this.description;
        types[1] = Types.VARCHAR;
        params[2] = this.dueDate;
        types[2] = Types.TIMESTAMP;
        params[3] = this.priority;
        types[3] = Types.INTEGER;
        params[4] = this.weighting;
        types[4] = Types.DOUBLE;
        params[5] = this.studyHours;
        types[5] = Types.INTEGER;
        params[6] = this.complete;
        types[6] = Types.BOOLEAN;
        // execute query
        if(Database.update(sql, params, types)){
            // get assignment ID
            sql = "SELECT assignmentID FROM courseAssignment WHERE courseID = ? AND description = ? AND dueDate = ?";
            params = new Object[3];
            types = new int[3];
            params[0] = this.courseID;
            types[0] = Types.INTEGER;
            params[1] = this.description;
            types[1] = Types.VARCHAR;
            params[2] = this.dueDate;
            types[2] = Types.TIMESTAMP;
            ResultSet rs = Database.query(sql, params, types); // if fetching the assignmentID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.assignmentID = rs.getInt("assignmentID");
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
     * Delete the assignment's details from the database.
     * @return true if successful, false otherwise.
     */
    public boolean delete() {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // delete associated study sessions (i.e. study sessions that were created for this assignment)
        String sql = "DELETE FROM studySession WHERE assignmentID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.assignmentID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)){
            return false;
        }
        // finally, delete the course assignment itself
        sql = "DELETE FROM courseAssignment WHERE assignmentID = ?";
        params = new Object[1];
        types = new int[1];
        params[0] = this.assignmentID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            return true;
        } else {
            System.out.println("Failed to delete course assignment for courseAssignmentID: " + this.assignmentID + ".");
            return false;
        }
    }
}
