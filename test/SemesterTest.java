import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.stms.web.Semester;
import java.time.LocalDate;

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
        LocalDate start = LocalDate.of(2018, 01, 19);

        LocalDate check = semester1.getStartDate();
        assertEquals(start, check);
    }

    @Test
    void setStartDate() {
        LocalDate x = LocalDate.of(2018, 9, 8);
        semester1.setStartDate(x);
        LocalDate y = semester1.getStartDate();
        assertEquals(x, y);
        x = LocalDate.of(2018, 01, 1);
        semester1.setStartDate(x);
    }

    @Test
    void getEndDate() {
        LocalDate end = LocalDate.of(2018, 06, 24);
        LocalDate check = semester1.getEndDate();
        assertEquals(end, check);
    }

    @Test
    void setEndDate() {
        LocalDate x = LocalDate.of(2018, 9, 8);
        semester1.setEndDate(x);
        LocalDate y = semester1.getEndDate();
        assertEquals(x, y);
        x = LocalDate.of(2018, 6, 24);
        semester1.setEndDate(x);

    }
}