package com.stms.web;

public class Lecture extends CourseSession {
	
	Lecture(){
		
	}
	
	public String getReminder(){
		String s = "Lecture from" + DateFormat(startDate) + " until " + DateFormat(endDate);
	}
}
