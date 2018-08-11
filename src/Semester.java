import java.util.Date;

public class Semester {

    private int semesterID;
    private String name;
    private Date startDate;
    private Date endDate;
<<<<<<< HEAD
    private Database DB;
=======
    private Course[] courses;
>>>>>>> 300a2411e37bce4b272f98aab334088f6e8d1eab

    // getters and setters for all variables

    public void setSemesterID(int ID) {
        this.semesterID = ID; }

    public int getSemesterID() {
        return semesterID; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setStartDate(Date date) {
        this.startDate = date; }

    public Date getStartDate() {
        return startDate; }

    public void setEndDate(Date date) {
        this.endDate = date; }

    public Date getEndDate() {
        return endDate; }

    public void setDB(Database db) {
        this.DB = db; }

    public Database getDB() {
        return DB; }

    // Various constructors

    Semester() { }

    Semester(int semesterID) {
<<<<<<< HEAD
        DB = new Database();

=======
        this.semesterID = semesterID;
>>>>>>> 300a2411e37bce4b272f98aab334088f6e8d1eab
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
