package com.stms.web;

public class Lecture extends CourseSession {
	
	Lecture(){
		super();
	}
	
	public String getReminder(){
		return "Lecture from" + DateFormat(super.getStartTime()) + " until " + DateFormat(super.getEndTime());
	}
}
