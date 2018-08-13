import java.sql.*;
import java.util.Date;

public class Course {

    private int courseID;
    private int semesterID;
    private String name;
    private String code;

    // Various constructors

    Course() { }

    Course(int courseID) {
        this.courseID = courseID;
    }

    /*public void addSession (CourseSession session) {

    }

    public void addAssignment (Assignment assignment) {

    }

    public CourseSession[] getSessions () {

    }

    public Assignment[] getAssignments() {

    }

    public boolean savetoDB() {

    }*/

    // getters and setters for all variables

    public void setSemesterID(int semesterID){
        this.semesterID = semesterID;
    }

    public void setCourseID(int ID) {
        this.courseID = ID; }

    public int getCourseID() {
        return courseID; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setCode(String code) {
        this.code = code; }

    public String getCode() {
        return code; }

}
