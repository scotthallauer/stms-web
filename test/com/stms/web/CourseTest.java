package com.stms.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course1;
    private Course course2;
    private Course course3;

    @BeforeEach
    void setUp() {
        try {
            course1 = new Course(1);
            course2 = new Course(4);
            course3 = new Course(7);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("All tests are invalid");
        }
    }

    @Test
    void getSessions() {
        assertNotNull(course1.getSessions());
    }

    @Test
    void getGradedSessions() {
        assertNotNull(course1.getGradedSessions());
    }

    @Test
    void getAssignments() {
        assertNotNull(course2.getAssignments());

    }

    @Test
    void getCourseID() {
        int x1 = course1.getCourseID();
        assertEquals(x1, 1);
    }

    @Test
    void getSemesterID1() {
        int semID1 = course2.getSemesterID1();
        assertEquals(semID1, 2);
    }

    @Test
    void setSemesterID1() {
        course1.setSemesterID1(5);
        int x  = course1.getSemesterID1();
        assertEquals(5, x);
        course1.setSemesterID1(1);
    }

    @Test
    void getName() {
        String s = course3.getName();
        assertEquals(s, "Computer Science");
    }

    @Test
    void setName() {
        course1.setName("test");
        String s = course1.getName();
        assertEquals("test", s);
        course1.setName("Computer Science");
        s = course1.getName();
        assertEquals("Computer Science", s);
    }

    @Test
    void getCode() {
        String code = course1.getCode();
        assertEquals(code, "CSC3002F");
    }

    @Test
    void setCode() {
        course1.setCode("Test");
        String s = course1.getCode();
        assertEquals("Test", s);
        course1.setCode("CSC3002F");
        s = course1.getCode();
        assertEquals(s, "CSC3002F");
    }

    @Test
    void getColour() {
        String s = course1.getColour();
        assertEquals("blue", s);
    }

    @Test
    void setColour() {
        course1.setColour("green");
        String s = course1.getColour();
        assertEquals("green", s);
        course1.setColour("blue");
    }

    //Test save and delete in the same test
    @Test
    void save() {
        Course course = new Course();
        course.setColour("green");
        course.setSemesterID1(3);
        course.setCode("CSC3302F");
        course.setName("Comsci");
        assertTrue(course.save());

        assertTrue(course.delete());
    }
}