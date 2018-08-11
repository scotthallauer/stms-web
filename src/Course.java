public class Course {

    private int courseID;
    private String name;
    private String code;

    // getters and setters for all variables

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

    // Various constructors

    Course() { }

    Course(int ID) {

    }

    public void addSession (CourseSession session) {

    }

    public void addAssignment (Assignment assignment) {

    }

    public CourseSession[] getSessions () {

    }

    public Assignment[] getAssignments() {

    }

    public boolean savetoDB() {

    }
}
