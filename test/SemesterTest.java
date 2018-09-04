import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SemesterTest {

    private Semester semester1;
    private Semester semester2;
    private Semester semester3;

    @BeforeEach
    void setUp() {
        try {
            semester1 = new Semester(1);
            semester2 = new Semester(4);
            semester3 = new Semester(5);
        } catch (Exception e){
             e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCourses() {
    }

    @Test
    void getSemesterID() {
        int ID1 = semester1.getSemesterID();
        int ID2 = semester2.getSemesterID();
        assertNotEquals(ID1, ID2);
        assertEquals(ID1, 1);
        assertEquals(ID2, 4);
    }

    @Test
    void getUserID() {
        int userID1 = semester1.getUserID();
        int userID2  = semester3.getUserID();
        assertEquals(1, userID1);
        assertEquals(3, userID2);
    }

    @Test
    void setUserID() {
        semester3.setUserID(1);
        assertEquals(semester1.getUserID(), semester3.getUserID());
        semester3.setUserID(3);
    }

    @Test
    void getName() {
        String s = semester1.getName();
        assertEquals("1st Semester", s);
    }

    @Test
    void setName() {
        semester3.setName("This is a test");
        assertEquals("This is a test", semester3.getName());
        semester3.setName("1st Semester");
    }

    @Test
    void getStartDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 01, 19, 0, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = semester1.getStartDate();
        assertEquals(start, check);
    }

    @Test
    void setStartDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        semester1.setStartDate(x);
        Timestamp y = semester1.getStartDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 01, 19, 0, 0);
        x = Timestamp.valueOf(temp);
        semester1.setStartDate(x);
    }

    @Test
    void getEndDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 06, 24, 0, 0);
        Timestamp end = Timestamp.valueOf(temp);
        Timestamp check = semester1.getEndDate();
        assertEquals(end, check);
    }

    @Test
    void setEndDate() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        semester1.setEndDate(x);
        Timestamp y = semester1.getEndDate();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 6, 24, 9, 0);
        x = Timestamp.valueOf(temp);
        semester1.setEndDate(x);

    }
}