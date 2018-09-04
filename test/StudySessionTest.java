import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudySessionTest {

    private StudySession SS1;
    private StudySession SS2;
    private StudySession SS3;

    @BeforeEach
    void setUp() {
        try {
            SS1 = new StudySession(1);
            SS2 = new StudySession(2);
            SS3 = new StudySession(3);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        StudySession s = new StudySession();
        s.setConfirmed(true);
        LocalDateTime temp = LocalDateTime.of(2018, 11, 7, 11, 0);
        Timestamp x = Timestamp.valueOf(temp);
        s.setStartTime(x);
        temp = LocalDateTime.of(2018, 11, 7, 12, 0);
        x = Timestamp.valueOf(temp);
        s.setEndTime(x);
        s.setCoursesessionID(9);
        s.setNote("Test 12");
        s.setSemesterID(2);
        assertTrue(s.save());
    }

    @Test
    void getStudySessionID() {
        int ID1 = SS1.getStudySessionID();
        int ID2 = SS3.getStudySessionID();
        assertEquals(1, ID1);
        assertEquals(3, ID2);
    }

    @Test
    void setSemesterID() {
        SS2.setSemesterID(5);
        int x  = SS2.getSemesterID();
        assertEquals(5, x);
        SS2.setSemesterID(2);
    }

    @Test
    void getSemesterID() {
        int x1 = SS2.getSemesterID();
        int x2 = SS3.getSemesterID();
        assertEquals(1, x1);
        assertEquals(5, x2);
        assertNotEquals(x1, x2);
    }

    @Test
    void setStartTime() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        SS1.setStartTime(x);
        Timestamp y = SS1.getStartTime();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 9, 4, 9, 0);
        x = Timestamp.valueOf(temp);
        SS1.setStartTime(x);
    }

    @Test
    void getStartTime() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 4, 9, 0);
        Timestamp start = Timestamp.valueOf(temp);
        Timestamp check = SS1.getStartTime();
        assertEquals(start, check);
    }

    @Test
    void setEndTime() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 8, 10, 0);
        Timestamp x = Timestamp.valueOf(temp);
        SS1.setEndTime(x);
        Timestamp y = SS1.getEndTime();
        assertEquals(x, y);
        temp = LocalDateTime.of(2018, 9, 4, 10, 0);
        x = Timestamp.valueOf(temp);
        SS1.setEndTime(x);
    }

    @Test
    void getEndTime() {
        LocalDateTime temp = LocalDateTime.of(2018, 9, 4, 10, 0);
        Timestamp end = Timestamp.valueOf(temp);
        Timestamp check = SS1.getEndTime();
        assertEquals(end, check);
        assertEquals(SS1.getEndTime(), SS2.getEndTime());
        assertNotEquals(SS1.getEndTime(), SS3.getEndTime());
    }

    @Test
    void setConfirmed() {
        assertTrue(SS2.getConfirmed());
        SS2.setConfirmed(false);
        assertFalse(SS2.getConfirmed());
        SS2.setConfirmed(true);

    }

    @Test
    void getConfirmed() {
        assertTrue(SS3.getConfirmed());
        assertTrue(SS2.getConfirmed());
    }

    @Test
    void setNote() {
        SS3.setNote("Test");
        String s = SS3.getNote();
        assertEquals("Test", s);
        SS3.setNote("Auto generated Study Session");
        assertEquals("Auto generated Study Session", SS3.getNote());
    }

    @Test
    void getNote() {
        assertEquals("Auto generated Study Session", SS1.getNote());
    }
}