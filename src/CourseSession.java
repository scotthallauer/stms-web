import java.time.LocalTime;

public class CourseSession {

    private int cSessionID;
    private String type;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note;
    private String location;
    private String rrule;
    private int weighting;
    private double possibleMark;
    private double earnedMark;
    private int priority;

    // getters and setters for all variables

    public void setcSessionID(int ID) {
        this.cSessionID = ID; }

    public int getcSessionID() {
        return cSessionID; }

    public void setType(String type) {
        this.type = type; }

    public String getType() {
        return type; }

    public void setName(String name) {
        this.name = name; }

    public String getName() {
        return name; }

    public void setStartTime(LocalTime time) {
        this.startTime = time; }

    public LocalTime getStartTime() {
        return startTime; }

    public void setEndTime(LocalTime time) {
        this.endTime = time; }

    public LocalTime getEndTime() {
        return endTime; }

    public void setLocation(String venue) {
        this.location = venue; }

    public String getLocation() {
        return location; }

    public void setNote(String notes) {
        this.note = notes; }

    public String getNote() {
        return note; }

    public void setRRule(String RRule) {
        this.rrule = RRule; }

    public String getRRule() {
        return rrule; }

    public void setWeighting(int weight) {
        this.weighting = weight; }

    public int getWeighting() {
        return weighting; }

    public void setPriority(int priority) {
        this.priority = priority; }

    public int getPriority() {
        return priority; }

    public void setPossibleMark(double possible) {
        this.possibleMark = possible; }

    public double getPossibleMark() {
        return possibleMark; }

    public void setEarnedMark(double earned) {
        this.earnedMark = earned; }

    public double getEarnedMark() {
        return earnedMark; }

    // Various constructors

    CourseSession() { }

    CourseSession(int ID) {

    }

    public boolean savetoDB() {

    }
}
