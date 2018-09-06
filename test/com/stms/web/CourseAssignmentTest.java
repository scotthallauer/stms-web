package com.stms.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CourseAssignmentTest {

    private CourseAssignment CA1;
    private CourseAssignment CA2;
    private CourseAssignment CA3;


    @BeforeEach
    void setUp() {
        try{
            CA1 = new CourseAssignment(1);
            CA2 = new CourseAssignment(2);
            CA3 = new CourseAssignment(3);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getAssignmentID() {
        int ID = CA1.getAssignmentID();
        assertEquals(1, ID);
    }

    @Test
    void setCourseID() {
        CA1.setCourseID(5);
        int ch = CA1.getCourseID();
        assertEquals(5, ch);
        CA1.setCourseID(1);
    }

    @Test
    void getCourseID() {
        int x  = CA2.getCourseID();
        assertEquals(6, x);
    }

    @Test
    void setDescription() {
        CA3.setDescription("Test");
        String s  = CA3.getDescription();
        assertEquals("Test", s);
        CA3.setDescription("gen prac");
        s  = CA3.getDescription();
        assertEquals("gen prac", s);
    }

    @Test
    void getDescription() {
        String s = CA2.getDescription();
        assertEquals("Capstone", s);
    }

    @Test
    void setDueDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        CA1.setDueDate(x);
        Timestamp y = CA1.getDueDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        x = Timestamp.valueOf(temp);
        CA1.setDueDate(x);
    }

    @Test
    void getDueDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = CA1.getDueDate();
        assertEquals(start, check);
    }

    @Test
    void setPriority() {
        CA2.setPriority(2);
        double x  = CA2.getPriority();
        assertEquals(2, x);
        CA2.setPriority(3);
        x  = CA2.getPriority();
        assertEquals(3, x);
    }

    @Test
    void getPriority() {
        double x  = CA2.getPriority();
        assertEquals(3, x);
    }

    @Test
    void setWeighting() {
        CA3.setWeighting(0.5);
        double x = CA3.getWeighting();
        assertEquals(0.5, x);
        CA3.setWeighting(1.5);
    }

    @Test
    void getWeighting() {
        double x = CA3.getWeighting();
        assertEquals(1.5, x);
    }

    @Test
    void setStudyHours() {
        CA1.setStudyHours(30);
        int x = CA1.getStudyHours();
        assertEquals(30, x);
        CA1.setStudyHours(0);
    }

    @Test
    void getStudyHours() {
        int x = CA2.getStudyHours();
        assertEquals(5, x);
    }

    @Test
    void setComplete() {
        CA1.setComplete(true);
        assertTrue(CA1.isComplete());
        CA1.setComplete(false);
    }

    @Test
    void isComplete() {
        assertTrue(CA3.isComplete());
        assertFalse(CA1.isComplete());
    }

    //test save and delete
    @Test
    void save() {
        CourseAssignment ca = new CourseAssignment();
        ca.setCourseID(3);
        ca.setDescription("Test");
        ca.setComplete(false);
        ca.setStudyHours(5);
        ca.setWeighting(1.5);
        LocalDateTime temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        Timestamp t = Timestamp.valueOf(temp);
        ca.setDueDate(t);
        assertTrue(ca.save());
        assertNotNull(ca.getAssignmentID());
        assertTrue(ca.delete());
    }
}