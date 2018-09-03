import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
            CS3 = new CourseSession(10);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getSessionID() {
        int x = CS1.getSessionID();
        assertEquals(1,x);
    }

    @Test
    void setSessionPID() {
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
    }

    @Test
    void getCourseID() {
        int x = CS3.getCourseID();
        assertEquals(3,x);
    }

    @Test
    void setType() {
    }

    @Test
    void getType() {
        String s = CS1.getType();
        assertEquals("lecture", s);
    }

    @Test
    void setStartDate() {}

    @Test
    void getStartDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 07, 23, 9, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = CS1.getStartDate();
        assertEquals(start, check);
    }

    @Test
    void setEndDate() {
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
    }

    @Test
    void getLength() {

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
    void setLocation() {
        CS1.setLocation("Menzies 5");
        assertNotNull(CS1.getLocation());
        assertEquals("Menzies 5", CS1.getLocation());
        CS1.setLocation(null);
    }

    @Test
    void getLocation() {
        String location = CS1.getLocation();
        assertNull(location);
    }

    @Test
    void setNote() {
        CS3.setNote("Test");
        assertNotNull(CS3.getNote());
        assertEquals("Test",CS3.getNote());
    }

    @Test
    void getNote() {
        String s = CS1.getNote();

        assertNull(s);

    }

    @Test
    void setPriority() {

    }

    @Test
    void getPriority() {
        int prio = CS3.getPriority();
        assertEquals(3, prio);
    }

    @Test
    void setWeighting() {
    }

    @Test
    void getWeighting() {
    }

    @Test
    void setPossibleMark() {
    }

    @Test
    void getPossibleMark() {
    }

    @Test
    void setEarnedMark() {
    }

    @Test
    void getEarnedMark() {
    }

    @Test
    void save() {
    }
}