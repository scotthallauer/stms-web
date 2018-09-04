package com.stms.web;

import java.time.LocalDateTime;

public class Occurrence {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Occurrence(LocalDateTime startDate, LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate(){
        return this.startDate;
    }

    public LocalDateTime getEndDate(){
        return this.endDate;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(!(other instanceof Occurrence)) return false;
        Occurrence otherOccurrence = (Occurrence)other;
        return this.startDate.equals(otherOccurrence.startDate) && this.endDate.equals(otherOccurrence.endDate);
    }
}
