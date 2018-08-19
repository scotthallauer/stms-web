package com.stms.web;import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.time.*;
import java.text.*;
import java.lang.Math;

/**
 * This class takes a course session ID as the parameter and calculates the priority for the session.
 *
 * Coded by Jonathon Everatt
 */

public class Priority {

    private int priority;
    private int prioritylevel;
    private int assignmentID;
    private Database DB;
    private Date today;

    Priority(int assignmentID, String Type){
        this.assignmentID = assignmentID;
        LocalDate dueDate = null;
        String sql = "";
        if (Type.equals("Assignment")){
            sql = "SELECT dueDate FROM stms.courseAssignment WHERE assignmentID = " + assignmentID + ";";
        } else if (Type.equals("Test")){
            sql = "SELECT * FROM stms.courseSession WHERE sessionID = " + assignmentID + ";";
        } else {
            System.out.println("Error if else block");
        }
        ResultSet rs = DB.query(sql);
        try{
             if(rs.next()){
                 dueDate = rs.getDate(1).toLocalDate();
             }
        } catch (SQLException e){
            System.out.println("Error in resultSet");
            e.printStackTrace();
        }
        DateUtil du = new DateUtil();
        du.calcDayNumInYear(dueDate);

        //SQL query format is correct and returns a date
        System.out.println("The dueDate is " + dueDate.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.today = new Date();
        System.out.println(dateFormat.format(this.today) + "HERE");

        System.out.println(this.today.toString());
        setPriorityLevel("High");
        priority = CalcPriority(dueDate);
        System.out.println("done");
    }



    private void setPriorityLevel(String level){
        if(level.equals("High")) {
            this.prioritylevel = 20;
        } else if (level.equals("Medium")) {
            this.prioritylevel = 10;
        }  else if (level.equals("Low")) {
            this.prioritylevel = 1;
        } else{
            System.out.println("Not priority level set. Invalid input setPrio ");
        }
    }

    private int CalcPriority(LocalDate due){
        DateUtil DU = new DateUtil();
        int daysLeft = DU.calcDayNumInYear(due) - DU.calcDayNumInYear(DU.getDateToday());
        System.out.println(daysLeft);

        double prio;
        prio = 100 + daysLeft;
        prio = Math.pow(prio, 0.5);
        System.out.println(prio);
        prio += prioritylevel;
        System.out.println(prio);
        //Current equation = 100x^1/2 + priolevel
        String s = "" + prio;
        if(s.indexOf('.') > 0){
            s = s.substring(0,s.indexOf('.'));
        }
        System.out.println(s);
        int temp = Integer.parseInt(s);
        System.out.println("" + temp);
        return  temp;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void setassignmentID(int assignmentID){
        this.assignmentID = assignmentID;
    }

    public int getPriority(){
        return priority;
    }

    public int getassignmentID(){
        return  assignmentID;
    }
}

