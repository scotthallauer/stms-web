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

}
