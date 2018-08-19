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
    private String note;
    private Double possibleMark;
    private Double earnedMark;
}
