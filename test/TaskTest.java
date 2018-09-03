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

    @AfterEach
    void tearDown() {
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
    void getName() {
        String s = task1.getName();
        assertEquals("Capstone", s);
    }

    @Test
    void setName() {
        task2.setName("Test");
        String s = task2.getName();
        assertEquals("Test", s);
        task2.setName("Capstone");
        s = task2.getName();
        assertEquals("Capstone", s);
    }

    @Test
    void getDeadline() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 7, 17, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = task1.getDeadline();
        assertEquals(start, check);
    }

    @Test
    void setDeadline() {
    }

    @Test
    void getCompleted() {
        assertFalse(task1.getCompleted());
    }

    @Test
    void setCompleted() {
        task3.setCompleted(false);
        assertFalse(task3.getCompleted());
        task3.setCompleted(true);
        assertTrue(task3.getCompleted());
    }

    @Test
    void getPriority() {
        double x =  task1.getPriority();
        assertEquals(80.0, x);
        assertEquals(task1.getPriority(), task2.getPriority());
    }

    @Test
    void setPriority() {
        task1.setPriority(1);
        double x = task1.getPriority();
        assertEquals(x, 1);
        task1.setPriority(80);
        x =  task1.getPriority();
        assertEquals(80.0, x);
    }

    @Test
    void getNote() {
        String s = task1.getNote();
        assertEquals("Final project", s);
    }

    @Test
    void setNote() {
        task3.setNote("Testing");
        String s = task3.getNote();
        assertEquals("Testing", s);
        task3.setNote("Genetics Test");
    }

    @Test
    void save() {
    }
}