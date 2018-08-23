package com.stms.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.time.*;
import java.text.*;
import java.lang.Math;

/**
 * This class takes a course session ID as the parameter and calculates the prioDuerity for the session.
 *
 * Coded by Jonathon Everatt
 */

public class Priority {

    private Double priority;
    private Double prioWeight;
    private Double prioritylevel;
    private int assignmentID;
    private Database DB;
    private Date today;
    private Utilities Util;

    /**
     *  Constructor for the class. Initializes  Date Utils and the prioDuerity level
     */
    Priority() {
        this.prioritylevel = 0.0;
        Util = new Utilities();

    }

    /**
     *  Main method for the class takes in the Due date and calculates the amount of days till it is due
     *  Then assigns a prioDuerity level to the duedate and returns.
     *
     * @param due Due date of the assignment that needs prioDuerity calculated
     * @return The prioDuerity for the assignment on curve 100x^0.5 or 150 if due in less than 2 days
     */
    public int CalcPriority(LocalDate due){
        int daysLeft = Util.calcDayNum(due) - Util.calcDayNum(Util.getDateToday());
        System.out.println(daysLeft + " is the amount of days left");
        if(daysLeft < 2){
            //Max absolutely top level priority must be completed
            return 100;
        }

        double prioDue;
        //y = 100(daysUntil -1) ^-.387
        prioDue = daysLeft - 1;
        //prioDue *= 100;
        System.out.println(prioDue + " here we go 1");
        prioDue = Math.pow(prioDue, -0.387);
        prioDue *= 100;
        System.out.println(prioDue + " here we go");

        Double prio = (0.65 * prioDue) + (0.2 * prioritylevel) + (0.15 * prioWeight);
        //Current equation = 100x^1/2 + prioDuelevel
        String s = "" + prioDue;
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

    public void setWeigthing(Double prioWeight){
        this.prioWeight = prioWeight;
    }

    public void setPriorityLevel(String level){ //If easier can set this to int with levels of 1,2,3
        if(level.equals("High")) {
            this.prioritylevel = 1.0;
        } else if (level.equals("Medium")) {
            this.prioritylevel = (2/3) * 1.0;
        }  else if (level.equals("Low")) {
            this.prioritylevel = (1/3) * 1.0;
        } else{
            System.out.println("Not priority  level set. Invalid input setPrio ");
        }
    }

    public void setPriority(Double priority){
        this.priority = priority;
    }

    public void setassignmentID(int assignmentID){
        this.assignmentID = assignmentID;
    }

    public Double getPriority(){
        return priority;
    }

    public int getassignmentID(){
        return  assignmentID;
    }
}
