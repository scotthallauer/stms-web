package com.stms.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.stms.web.CourseSession;

import static org.junit.jupiter.api.Assertions.*;

class CourseSessionTest {

    private CourseSession CS1;
    private CourseSession CS2;
    private CourseSession CS3;

    @BeforeEach
    void setUp() {
        try{
            CS1 = new CourseSession(1);
            CS2 = new CourseSession(8);
            CS3 = new CourseSession(5);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSessionID() {
        int x = CS1.getSessionID();
        assertEquals(1,x);
    }

    @Test
    void setSessionPID() {
        CS1.setSessionPID(4);
        int x = CS1.getSessionPID();
        assertEquals(4, x);
        CS1.setSessionPID(0);
        x = CS1.getSessionPID();
        assertEquals(0, x);
    }

    @Test
    void getSessionPID() {
        int ID = CS2.getSessionPID();
        int ID2 = CS1.getSessionPID();
        assertEquals(1, ID);
        assertEquals(0, ID2);
    }

    @Test
    void setCourseID() {
        CS1.setCourseID(1);
        int x = CS1.getCourseID();
        assertEquals(1, x);
        CS1.setCourseID(3);
        x = CS1.getCourseID();
        assertEquals(3, x);
    }

    @Test
    void getCourseID() {
        int x = CS3.getCourseID();
        assertEquals(5,x);
    }


    @Test
    void setType() {
        CS2.setType("Test");
        String s= CS2.getType();
        assertEquals("test", s);//Force all types to lowercase
        CS2.setType("lecture");
        s= CS2.getType();
        assertEquals("lecture", s);

    }

    @Test
    void getType() {
        String s = CS1.getType();
        assertEquals("lecture", s);
    }

    @Test
    void setStartDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        CS1.setStartDate(x);
        Timestamp y = CS1.getStartDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 7, 23, 9, 0);
        x = Timestamp.valueOf(temp);
        CS1.setStartDate(x);
    }

    @Test
    void getStartDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 07, 23, 9, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = CS1.getStartDate();
        assertEquals(start, check);
    }

    @Test
    void setEndDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        CS1.setEndDate(x);
        Timestamp y = CS1.getEndDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 10, 7, 9, 0);
        x = Timestamp.valueOf(temp);
        CS1.setEndDate(x);
    }

    @Test
    void getEndDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 10, 7, 9, 45);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = CS1.getEndDate();
        assertEquals(start, check);
    }

    @Test
    void setLength() {
        long x1 = 3000;
        CS1.setLength(x1);
        long x2 = CS1.getLength();
        assertEquals(x1, x2);
        x1 = 2700;
        CS1.setLength(x1);
    }

    @Test
    void getLength() {
        long x1 = 2700;
        long x2 = CS1.getLength();
        assertEquals(x1, x2);

    }

    @Test
    void setRecType() {
        CS1.setRecType("Test");
        String s = CS1.getRecType();
        assertEquals("Test", s);
        CS1.setRecType("week_1___1,2,3,4,5#");

    }

    @Test
    void getRecType() {
        String rec = CS1.getRecType();
        assertEquals("week_1___1,2,3,4,5#", rec);
    }

    @Test
    void setPriority() {
        CS2.setPriority(2);
        int x = CS2.getPriority();
        assertEquals(2, x);
        CS2.setPriority(0);

    }

    @Test
    void getPriority() {
        int prio = CS1.getPriority();
        assertEquals(2, prio);
    }

    @Test
    void setWeighting() {
        CS1.setWeighting(0.25);
        double x = CS1.getWeighting();
        assertEquals(0.25, x);
        CS1.setWeighting(0.2);
    }

    @Test
    void getWeighting() {
        double x = CS3.getWeighting();
        assertEquals(0.1, x);
    }

    @Test
    void setStudyHours() {
        CS1.setStudyHours(500);
        int x = CS1.getStudyHours();
        assertEquals(500, x);
        CS1.setStudyHours(10);
    }

    @Test
    void getStudyHours() {
         int x = CS1.getStudyHours();
         assertEquals(10, x);
    }

    @Test
    void isGraded() {
        assertTrue(CS3.isGraded());
    }

    //Tests save and delete methods
    @Test
    void save() {
        CourseSession CS = new CourseSession();
        CS.setStudyHours(5);
        CS.setCourseID(3);
        LocalDateTime temp = LocalDateTime.of(2018, 10, 7, 9, 45);
        Timestamp t= Timestamp.valueOf(temp);
        CS.setEndDate(t);
        temp = LocalDateTime.of(2018, 10, 7, 9, 0);
        t= Timestamp.valueOf(temp);
        CS.setStartDate(t);
        CS.setType("test");
        long x = 2700;
        CS.setLength(x);
        CS.setRecType("week_1___1,2,3,4,5#");
        CS.setPriority(2);
        CS.setWeighting(0.15);
        CS.setSessionPID(0);
        assertTrue(CS.save());
        assertTrue(CS.delete());
    }


}