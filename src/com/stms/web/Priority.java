package com.stms.web;

import java.time.temporal.ChronoUnit;
import java.time.*;
import java.lang.Math;

/**
 * This class takes a course session ID as the parameter and calculates the prioDuerity for the session.
 * Coded by Scott Hallauer and Jonathon Everatt
 */
public class Priority {

    /**
     * Main method for the class takes in the Due date and calculates the amount of days till it is due
     * Then assigns a prioDuerity level to the duedate and returns.
     * @param dueDate Due date of the assignment that needs prioDuerity calculated
     * @return The prioDuerity for the assignment on curve 100x^0.5 or 150 if due in less than 2 days
     */
    public static double calculate(LocalDate dueDate, Integer userPriority, Double weighting){

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        double datePriority;
        if(daysLeft <= 2){
            datePriority = 100.0;
        }else{
            datePriority = 100.0 * Math.pow(daysLeft - 1, -0.387); // = 100 * (daysLeft-1)^-0.387
        }

        if(userPriority == null){
            userPriority = 2; // default to 'normal' priority
        }

        double overallPriority;
        if(weighting == null){
            overallPriority = (0.7 * (datePriority/100.0)) + (0.3 * (userPriority/3.0));
        }else{
            overallPriority = (0.65 * (datePriority/100.0)) + (0.2 * (userPriority/3.0)) + (0.15 * weighting/100.0);
        }

        return overallPriority;

    }

}
