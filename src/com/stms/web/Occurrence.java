package com.stms.web;

import java.time.LocalDateTime;

/**
 * Class that holds a timeslot from a User. Serves as a container for each and every occuerence of a course
 * session. Repeating sessions repeating an occurence for every time it repeats
 */
public class Occurrence {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * Constructor for occurence. Just hold beginning and end for the timeslot
     *
     * @param startDate when the occurence begins
     * @param endDate when the occurence ends
     */
    public Occurrence(LocalDateTime startDate, LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Getters for the class
     */

    public LocalDateTime getStartDate(){
        return this.startDate;
    }

    public LocalDateTime getEndDate(){
        return this.endDate;
    }

    /**
     * Checks to see whether or not the other object has the same start and end time as this occurence
     *
     * @param other checks if other object holds the same timesolt as this one
     * @return true if during the same time and false if different time
     */
    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(!(other instanceof Occurrence)) return false;
        Occurrence otherOccurrence = (Occurrence)other;
        return this.startDate.equals(otherOccurrence.startDate) && this.endDate.equals(otherOccurrence.endDate);
    }
}
