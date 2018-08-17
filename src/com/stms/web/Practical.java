package com.stms.web;

public class Practical extends CourseSession {
	String[] bringToPrac;
	
	Practical(){
		super();
	}
	
	public void PracRequirements(String[] requirements){
        System.out.print("Don't forget to do these before you prac");
        for (int x = 0; x < requirements.length; x++){
            System.out.print(requirements[x]);
        }
    }
	
	public String getReminder(){
		String s = "Practical from" + DateFormat(super.getStartTime()) + " until " + DateFormat(super.getEndTime());
		return s;
	}
	
	public void setBringToPrac(String[] bring){
		bringToPrac = bring;
	}
	
	public String remindBring(){
		String s = "You need to bring: ";
		for (int x = 0; x < bringToPrac.length; x++){
			s += bringToPrac[x] + ", ";
		}
		return s;
	}
	
}
