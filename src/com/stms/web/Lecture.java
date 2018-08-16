package com.stms.web;

public class Lecture extends CourseSession {
	
	Lecture(){
		super();
	}
	
	public String getReminder(){
		return "Lecture from" + super.DateFormat(super.getStartTime()) + " until " + super.DateFormat(super.getEndTime());
	}
}
