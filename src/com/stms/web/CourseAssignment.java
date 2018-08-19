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
    private String name;
    private Timestamp dueDate;
    private Integer priority;
    private Double possibleMark;
    private Double earnedMark;
    private Double weighting;
    private String note;

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
    CourseAssignment(int assignmentID) throws Exception {
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
            this.name = rs.getString("assignmentName");
            this.dueDate = rs.getTimestamp("dueDate");
            this.priority = rs.getInt("priority");
            this.possibleMark = rs.getDouble("possibleMark");
            this.earnedMark = rs.getDouble("earnedMark");
            this.weighting = rs.getDouble("weighting");
            this.note = rs.getString("note");
        } else{
            throw new NullPointerException("No CourseAssignment exists with the assignmentID " + assignmentID);
        }
    }
}
