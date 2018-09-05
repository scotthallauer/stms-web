package com.stms.web;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        try{
            task1 = new Task(1);
            task2 = new Task(2);
            task3 = new Task(3);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getTaskID() {
        int x = task1.getTaskID();
        int y = task2.getTaskID();
        assertEquals(1, x);
        assertEquals(2, y);
    }

    @Test
    void getUserID() {
        int x = task1.getUserID();
        int y = task2.getUserID();
        assertEquals(1, x);
        assertEquals(2, y);
    }

    @Test
    void setUserID() {
        task3.setUserID(3);
        assertEquals(3, task3.getUserID());
        task3.setUserID(1);
        assertEquals(1, task3.getUserID());
    }

    @Test
    void getDescription() {
        String s = task1.getDescription();
        assertEquals("Capstone final project", s);
    }

    @Test
    void setDescription() {
        task3.setDescription("Testing");
        String s = task3.getDescription();
        assertEquals("Testing", s);
        task3.setDescription("Genetics Test");
    }

    @Test
    void getDueDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = task1.getDueDate();
        assertEquals(start, check);
    }

    @Test
    void setDueDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        task1.setDueDate(x);
        Timestamp y = task1.getDueDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        x = Timestamp.valueOf(temp);
        task1.setDueDate(x);
    }

    @Test
    void isComplete() {
        assertFalse(task1.isComplete());
        assertFalse(task3.isComplete());
    }

    @Test
    void setComplete() {
        task3.setComplete(true);
        assertTrue(task3.isComplete());
        task3.setComplete(false);
    }

    @Test
    void save() {
        Task task = new Task();
        task.setUserID(1);
        task.setDescription("Test");
        LocalDateTime temp = LocalDateTime.of(2018, 11, 7, 11, 0);
        Timestamp x = Timestamp.valueOf(temp);
        task.setDueDate(x);
        task.setComplete(false);
        assertTrue(task.save());
        assertTrue(task.delete());
    }

}