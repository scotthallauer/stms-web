import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
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
    void setName() {
        CA3.setName("Test");
        String s  = CA3.getName();
        assertEquals("Test", s);
        CA3.setName("gen prac");
        s  = CA3.getName();
        assertEquals("gen prac", s);
    }

    @Test
    void getName() {
        String s = CA2.getName();
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
        CA2.setPriority(79);
        double x  = CA2.getPriority();
        assertEquals(79, x);
        CA2.setPriority(80);
        x  = CA2.getPriority();
        assertEquals(80.0, x);
    }

    @Test
    void getPriority() {
        double x  = CA2.getPriority();
        assertEquals(80.0, x);
    }

    @Test
    void setPossibleMark() {
        CA2.setPossibleMark(51.0);
        double x = CA2.getPossibleMark();
        assertEquals(51.0, x);
        CA2.setPossibleMark(50.0);
        x = CA2.getPossibleMark();
        assertEquals(50.0, x);
    }

    @Test
    void getPossibleMark() {
        double x = CA2.getPossibleMark();
        assertEquals(50.0, x);
    }

    @Test
    void setEarnedMark() {
        CA3.setEarnedMark(100.0);
        double x = CA3.getEarnedMark();
        assertEquals(100.0, x);
        CA3.setEarnedMark(45.0);
        x = CA3.getEarnedMark();
        assertEquals(45.0, x);
    }

    @Test
    void getEarnedMark() {
        double x =  CA1.getEarnedMark();
        assertEquals(0.0, x);
        x = CA3.getEarnedMark();
        assertEquals(45.0, x);
    }

    @Test
    void setWeighting() {
        CA2.setWeighting(0.4);
        double x = CA2.getWeighting();
        assertEquals(0.4, x);
        CA2.setWeighting(0.2);
        x = CA2.getWeighting();
        assertEquals(0.2, x);
    }

    @Test
    void getWeighting() {
        double x = CA1.getWeighting();
        double y = CA3.getWeighting();
        assertEquals(0.2, x);
        assertEquals(0.1, y);
    }

    @Test
    void setNote() {
        CA2.setNote("Test");
        String s = CA2.getNote();
        assertEquals("Test", s);
        CA2.setNote("Final project");
        s = CA2.getNote();
        assertEquals("Final project", s);
    }

    @Test
    void getNote() {
        String s = CA2.getNote();
        assertEquals("Final project", s);
        s = CA3.getNote();
        assertEquals("Genetics Prac", s);
    }

    @Test
    void setCompleted() {
        CA1.setCompleted(true);
        assertTrue(CA1.getCompleted());
        CA1.setCompleted(false);
        assertFalse(CA1.getCompleted());
    }

    @Test
    void getCompleted() {
        assertTrue(CA3.getCompleted());
        assertFalse(CA2.getCompleted());
    }

    @Test
    void save() {
    }
}