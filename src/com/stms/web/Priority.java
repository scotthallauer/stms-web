package com.stms.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.lang.Math;

/**
 * This class takes a course session ID as the parameter and calculates the priority for the session.
 *
 * C oded by Jonathon Everatt
 */

public class Priority {

    private int priority;
    private int prioritylevel;
    private int assignmentID;
    private Database DB;

    Priority(int assignmentID){
        this.assignmentID = assignmentID;
        Date dueDate = null;
        DB = new Database();
        String sql = "SELECT dueDate FROM stms.courseAssignment WHERE assignmentID = " + assignmentID + ";";
        ResultSet rs = DB.query(sql);
        try{
             if(rs.next()){
                 dueDate = rs.getTimestamp(1);
             }
        } catch (SQLException e){
            e.printStackTrace();
        }
        //SQL query format is correct and returns a date
        System.out.println("The Date is " + dueDate.toString());
        Date today;// = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        today = calendar.getTime();
        System.out.println(today.toString());
        setPriorityLevel("High");
        priority = CalcPriority(20);
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

    private int CalcPriority(int daysLeft){
        double prio;
        prio = 100 + daysLeft;
        prio = Math.pow(prio, 0.5);
        prio += prioritylevel;
        System.out.println(prio);
        //Current equation = 100x^1/2 + priolevel
        String s = "" + prio;
        s = s.substring(0,s.indexOf('.'));
        System.out.println(s);
        int temp = Integer.parseInt(s);
        System.out.println("" + temp);
		if (temp > 150){
			temp = 150;
		}
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
