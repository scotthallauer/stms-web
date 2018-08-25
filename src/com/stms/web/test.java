package com.stms.web;

public class test {
    public static void main (String [] args) {

        try {
            Semester sem = new Semester(2);
            boolean b = sem.deleteSemester();
        } catch (Exception e) {}
    }
}
