package com.stms.web;

import java.sql.ResultSet;
import java.sql.ResultSet;
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
    private DateUtil DU;

    /**
     *  Constructor for the class. Initializes  Date Utils and the priority level
     */
    Priority() {
        this.prioritylevel = 0;
        DU = new DateUtil();

    }

    /**
     *  Main method for the class takes in the Due date and calculates the amount of days till it is due
     *  Then assigns a priority level to the duedate and returns.
     *
     * @param due Due date of the assignment that needs priority calculated
     * @return The priority for the assignment on curve 100x^0.5 or 150 if due in less than 2 days
     */
    public int CalcPriority(LocalDate due){
        int daysLeft = DU.calcDayNumInYear(due) - DU.calcDayNumInYear(DU.getDateToday());
        System.out.println(daysLeft);
        if(daysLeft < 2){
            //Max absolutely top level priority must be completed
            return 150;
        }

        double prio;
        prio = 100 + daysLeft;
        prio = Math.pow(prio, 0.5);
        prio += prioritylevel;
        //Current equation = 100x^1/2 + priolevel
        String s = "" + prio;
        if(s.indexOf('.') > 0){
            s = s.substring(0,s.indexOf('.'));
        }
        int temp = Integer.parseInt(s);
        return  temp;
    }

    /**
     * Set and get methods for the class
     *
     */

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
