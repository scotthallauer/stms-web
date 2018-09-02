import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
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
    }

    @Test
    void getSemesterID2() {
        int test = course3.getSemesterID2();
        assertNull(test);
    }

    @Test
    void setSemesterID2() {
    }

    @Test
    void getName() {
        String s = course3.getName();
        assertEquals(s, "Computer Science");
    }

    @Test
    void setName() {
    }

    @Test
    void getCode() {
        String code = course1.getCode();
        assertEquals(code, "CSC3002F");
    }

    @Test
    void setCode() {
    }

    @Test
    void getColour() {
    }

    @Test
    void setColour() {
    }
}