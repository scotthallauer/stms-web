import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;

public class Semester {

    private String semesterID;
    private String name;
    private Date startDate;
    private Date endDate;
    private Database DB;
    //private Course[] courses;

    // Various constructors

    Semester() { }

    Semester(String semesterID) {
        DB = new Database();
        this.semesterID = semesterID;

        System.out.println("SemesterID ONLY constructor test");

        String query = "SELECT * FROM stms1.semester WHERE semesterID = '" + semesterID + "';";
        ResultSet rs = DB.filterDB(query);
        try {
            if (rs.next()){
                try{
                    this.semesterID = rs.getString(1);
                    name = rs.getString(3);
                    startDate = rs.getTimestamp(4);
                    endDate = rs.getTimestamp(5);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        } catch
        (SQLException e){
            e.printStackTrace();
            System.out.println("Failed SEMESTERID ONLY CONSTRUCTOR");
        }
        System.out.println(semesterID + " name : " + name);
    }

    Semester(String semesterID, String name, Date start, Date end){
        DB = new Database();
        this.semesterID = semesterID;
        this.name = name;
        startDate = start;
        endDate = end;

        System.out.println("Semester object created");
    }

    // getters and setters for all variables

    /*public void setSemesterID(int ID) {
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

    public void setCourses(Course[] crs) {
        this.courses = crs; }

    public Course[] getCourses() {
        return courses; }


    public void addCourse (Course course) {

    }

    public StudySession[] getStudySession() {

    }

    public boolean savetoDB() {

    }*/
}
