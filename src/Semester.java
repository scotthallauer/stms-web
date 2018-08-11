import java.util.Date;

public class Semester {

    private int semesterID;
    private String name;
    private Date startDate;
    private Date endDate;
    private Course[] courses;

    Semester() {

    }

    Semester(int semesterID) {
        this.semesterID = semesterID;
    }

    public void addCourse (Course course) {

    }

    public Course[] getCourses() {

    }

    public StudySession[] getStudySession() {

    }

    public boolean savetoDB() {

    }
}
