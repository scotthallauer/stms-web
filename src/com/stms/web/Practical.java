package com.stms.web;

public class Practical extends CourseSession {
	
	public void PracRequirements(String[] requirements){
        System.out.print("Don't forget to do these before you prac");
        for (int x = 0; x < requirements.length; x++){
            System.out.print(requirements[x]);
        }
    }
	
}
