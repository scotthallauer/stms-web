package com.stms.web;

import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.*;
import java.util.TimeZone;

/**
 * CourseSession class for Student Time Management System
 * Used to create new and edit existing course sessions in the database.
 * @author Jonathon Everatt, Scott Hallauer and Jessica Bourn
 * @version 18/08/2018
 */
public class CourseSession {

    // ATTRIBUTES //

    private Boolean recordExists;
    private Boolean recordSaved;
    private CourseSession[] childSessions; // if this is a recurring event, child sessions are occurrences that have been modified or deleted by the user

    private Integer sessionID;
    private Integer sessionPID;
    private Integer courseID;
    private String type;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long length;
    private String recType;
    private Integer priority;
    private Double weighting;
    private Integer studyHours;

    // CONSTRUCTORS //

    /**
     * Blank constructor used to create and insert new course session records in the database.
     */
    public CourseSession(){
        this.recordExists = false;
        this.recordSaved = false;
    }

    /**
     * Parameterised constructor used to create and fetch any existing course sessions from the database.
     * @param sessionID the course session's unique ID in the database
     */
    public CourseSession(int sessionID) throws Exception {
        // check if database is connected
        if(!Database.isConnected()) {
            throw new SQLException("Database is not connected.");
        }
        // query database to get course session details (if course session exists)
        String sql = "SELECT * FROM courseSession WHERE sessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = sessionID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        if (rs.first()) {
            this.sessionID = rs.getInt("sessionID");
            this.sessionPID = rs.getInt("sessionPID");
            this.courseID = rs.getInt("courseID");
            this.type = rs.getString("sessionType");
            this.startDate = rs.getTimestamp("startDate");
            this.endDate = rs.getTimestamp("endDate");
            this.length = rs.getLong("length");
            if(rs.wasNull()){
                this.length = null;
            }
            this.recType = rs.getString("recType");
            if(rs.wasNull()){
                this.recType = null;
            }
            this.priority = rs.getInt("priority");
            if(rs.wasNull()){
                this.priority = null;
            }
            this.weighting = rs.getDouble("weighting");
            if(rs.wasNull()){
                this.weighting = null;
            }
            this.studyHours = rs.getInt("studyHours");
            if(rs.wasNull()){
                this.studyHours = null;
            }
            this.recordExists = true;
            this.recordSaved = true;
            this.loadChildSessions();
        }else{
            throw new NullPointerException("No CourseSession exists with the sessionID " + sessionID);
        }
    }

    /**
     * Loads all the sessions that have this CourseSession's ID as their parentID. Mainly used for deleted sessions
     * that are associated to this using the DHTMLX format
     */
    private void loadChildSessions(){
        // check if database is connected
        if(Database.isConnected()) {
            String sql = "SELECT sessionID FROM courseSession WHERE sessionPID = ?";
            Object[] params = new Object[1];
            int[] types = new int[1];
            params[0] = this.sessionID;
            types[0] = Types.INTEGER;
            ResultSet rs = Database.query(sql, params, types);
            ArrayList<CourseSession> childSessions = new ArrayList<CourseSession>();
            try {
                while(rs.next()){
                    childSessions.add(new CourseSession(rs.getInt("sessionID")));
                }
            }catch(Exception e){
                System.out.println("Failed to load child sessions for CourseSession (sessionID: " + this.sessionID + ").");
                e.printStackTrace();
            }
            this.childSessions = childSessions.toArray(new CourseSession[0]);
        }
    }

    /**
     * Getters and setters for the class
     */
    public Integer getSessionID() {
        return this.sessionID;
    }

    public void setSessionPID(Integer sessionPID){
        if(sessionPID == null) sessionPID = 0;
        if(sessionPID >= 0) {
            this.sessionPID = sessionPID;
            this.recordSaved = false;
        }
    }

    public Integer getSessionPID() {
        return this.sessionPID;
    }

    public void setCourseID(Integer courseID){
        if(courseID != null && courseID >= 1) {
            this.courseID = courseID;
            this.recordSaved = false;
        }
    }

    public Integer getCourseID(){
        return this.courseID;
    }

    public void setType(String type) {
        if(type != null && type.length() > 1) {
            this.type = type.toLowerCase();
            this.recordSaved = false;
        }
    }

    public String getType() {
        return this.type;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        this.recordSaved = false;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        this.recordSaved = false;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setLength(Long length) {
        if(length == null || length > 0) {
            this.length = length;
            this.recordSaved = false;
        }
    }

    public Long getLength() {
        return this.length;
    }

    public void setRecType(String recType) {
        if(recType == null || recType.length() > 0){
            this.recType = recType;
            this.recordSaved = false;
        }
    }

    public String getRecType() {
        return this.recType;
    }

    public void setPriority(Integer priority){
        if(priority == null || (priority >= 1 && priority <= 3)){
            this.priority = priority;
            this.recordSaved = false;
        }
    }

    public Integer getPriority(){
        return this.priority;
    }

    public void setWeighting(Double weighting){
        if(weighting == null || (weighting >= 0.0 && weighting <= 100.0)){
            this.weighting = weighting;
            this.recordSaved = false;
        }
    }

    public Double getWeighting(){
        if(this.weighting == null){
            return 0.0;
        }else {
            return this.weighting;
        }
    }

    public void setStudyHours(Integer studyHours){
        if(studyHours == null || studyHours > 0){
            this.studyHours = studyHours;
            this.recordSaved = false;
        }
    }

    public Integer getStudyHours(){
        if(this.studyHours == null){
            return 0;
        }else {
            return this.studyHours;
        }
    }

    public boolean isGraded(){
        return (this.priority != null);
    }

    /**
     * Deletes old study sessions that are associated to this course session and calls scheduler to reschedule the
     * amount of hours necessary for this application. This method is used when Users move the CourseSession with
     * studySessions already scheduled
     *
     * @return true if all study sessions are scheduled and false if not all could be scheduled
     */
    public boolean scheduleStudySessions(){

        // delete old study sessions associated with this course session
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM studySession WHERE courseSessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.sessionID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)){
            return false;
        }

        if(this.isGraded()){

            // schedule new study sessions
            try {
                int userID = new Semester(new Course(this.courseID).getSemesterID1()).getUserID();
                Scheduler scheduler = new Scheduler(userID);
                Occurrence[] occurrences = this.getOccurrences(1000);
                int totalUnscheduledHours = 0;
                for(int j = 0 ; j < occurrences.length ; j++) {
                    int unscheduledHours = this.studyHours;
                    for (int i = 0; i < 5 && unscheduledHours > 0; i++) { // give the scheduling algorithm 5 chances to schedule all required hours
                        unscheduledHours = scheduler.generateSessions(unscheduledHours, occurrences[j].getStartDate(), "coursesession", this.sessionID);
                    }
                    totalUnscheduledHours += unscheduledHours;
                }
                if(totalUnscheduledHours > 0){
                    return false;
                }else{
                    return true;
                }
            }catch(Exception e){
                return false;
            }
        }
        return false;
    }

    /**
     * Get all of the occurrences for this session.
     * @param max the maximum of occurrences to return (you can probably just always set this to 1000)
     * @return an array of occurrences for this session
     */
    public Occurrence[] getOccurrences(int max){

        LocalDateTime startDate = this.startDate.toLocalDateTime();
        LocalDateTime endDate = this.endDate.toLocalDateTime();

        ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
        Occurrence period;

        if(this.recType == null || this.recType.length() == 0){

            period = new Occurrence(startDate, endDate);
            occurrences.add(period);

        }else if(!this.recType.equals("none")){

            String type = this.recType.split("_")[0];
            Integer count;
            try {
                count = Integer.valueOf(this.recType.split("_")[1]);
            }catch(Exception e){
                count = null;
            }
            Integer day;
            try {
                day = Integer.valueOf(this.recType.split("_")[2]);
            }catch(Exception e){
                day = null;
            }
            Integer count2;
            try {
                count2 = Integer.valueOf(this.recType.split("_")[3]);
            }catch(Exception e){
                count2 = null;
            }
            Integer[] days;
            try {
                String[] strDays = this.recType.split("_")[4].replace("#", "").split(",");
                days = new Integer[strDays.length];
                for(int i = 0 ; i < strDays.length ; i++){
                    days[i] = Integer.valueOf(strDays[i]);
                }
            }catch(Exception e){
                days = null;
            }
            String extra;
            try {
                extra = this.recType.split("#")[1];
            }catch (Exception e){
                extra = "";
            }

            if(type.equals("day")){

                // add first occurrence
                period = new Occurrence(startDate, startDate.plusSeconds(this.length));
                occurrences.add(period);

                // add all other occurrences
                boolean flag = false;
                for(int i = 1 ; i < max && !flag ; i++){
                    LocalDateTime start = period.getStartDate().plusDays(count);
                    LocalDateTime end = start.plusSeconds(this.length);
                    if(end.isBefore(endDate) || end.isEqual(endDate)) {
                        period = new Occurrence(start, end);
                        occurrences.add(period);
                    }else{
                        flag = true;
                    }
                }

            }else if(type.equals("week")){

                if(days.length > 0) {
                    // add first occurrence
                    period = new Occurrence(startDate.plusDays(days[0] - 1), startDate.plusSeconds(this.length));
                    occurrences.add(period);

                    // add all other occurrences
                    boolean flag = false;
                    for (int i = 1; i < max && !flag; i++) {
                        for (int j = 1; j < days.length && i < max && !flag; j++) {
                            LocalDateTime start = period.getStartDate().plusDays(days[j] - days[j - 1]);
                            LocalDateTime end = start.plusSeconds(this.length);
                            if (end.isBefore(endDate) || end.isEqual(endDate)) {
                                period = new Occurrence(start, end);
                                occurrences.add(period);
                                i++;
                            } else {
                                flag = true;
                            }
                        }
                        LocalDateTime start = period.getStartDate().minusDays(days[days.length - 1] - days[0]).plusWeeks(count);
                        LocalDateTime end = start.plusSeconds(this.length);
                        if (end.isBefore(endDate) || end.isEqual(endDate)) {
                            period = new Occurrence(start, end);
                            occurrences.add(period);
                        } else {
                            flag = true;
                        }
                    }
                }


            }else if(type.equals("month")){

                // monthly on same day number
                if(day == null || count2 == null) {

                    // add first occurrence
                    period = new Occurrence(startDate, startDate.plusSeconds(this.length));
                    occurrences.add(period);

                    // add all other occurrences
                    boolean flag = false;
                    for (int i = 1; i < max && !flag; i++) {
                        LocalDateTime start = period.getStartDate().plusMonths(count);
                        LocalDateTime end = start.plusSeconds(this.length);
                        if (end.isBefore(endDate) || end.isEqual(endDate)) {
                            period = new Occurrence(start, end);
                            occurrences.add(period);
                        } else {
                            flag = true;
                        }
                    }

                }

                // monthly on same week day
                else{

                    // get the date of the first occurrence
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, startDate.getYear());
                    calendar.set(Calendar.MONTH, startDate.getMonthValue()-1);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, count2);
                    calendar.set(Calendar.DAY_OF_WEEK, day+1);
                    calendar.set(Calendar.HOUR_OF_DAY, startDate.getHour());
                    calendar.set(Calendar.MINUTE, startDate.getMinute());
                    calendar.set(Calendar.SECOND, startDate.getSecond());
                    calendar.set(Calendar.MILLISECOND, 0);

                    // add first occurrence
                    LocalDateTime start = Instant.ofEpochMilli(calendar.getTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime end = start.plusSeconds(this.length);
                    period = new Occurrence(start, end);
                    occurrences.add(period);

                    // add all other occurrences
                    boolean flag = false;
                    for (int i = 1; i < max && !flag; i++) {
                        LocalDateTime approxDate = period.getStartDate().plusMonths(count);
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, approxDate.getYear());
                        calendar.set(Calendar.MONTH, approxDate.getMonthValue()-1);
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, count2);
                        calendar.set(Calendar.DAY_OF_WEEK, day+1);
                        calendar.set(Calendar.HOUR_OF_DAY, approxDate.getHour());
                        calendar.set(Calendar.MINUTE, approxDate.getMinute());
                        calendar.set(Calendar.SECOND, approxDate.getSecond());
                        calendar.set(Calendar.MILLISECOND, 0);
                        start = Instant.ofEpochMilli(calendar.getTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        end = start.plusSeconds(this.length);
                        if (end.isBefore(endDate) || end.isEqual(endDate)) {
                            period = new Occurrence(start, end);
                            occurrences.add(period);
                        } else {
                            flag = true;
                        }
                    }

                }



            }else if(type.equals("year")){

                // our app doesn't support annually recurring events, so this functionality is unnecessary

            }

        }

        for(int i = 0 ; i < this.childSessions.length ; i++) {
            // remove occurrences that have been deleted by the user
            if(this.childSessions[i].getOccurrences(1000).length == 0) {
                LocalDateTime childStart = this.childSessions[i].getStartDate().toLocalDateTime();
                LocalDateTime childEnd = this.childSessions[i].getEndDate().toLocalDateTime();
                occurrences.removeIf(o -> o.equals(new Occurrence(childStart, childEnd)));
            // replace occurrences that have been modified by the user
            }else{
                LocalDateTime childNewStart = this.childSessions[i].getStartDate().toLocalDateTime();
                LocalDateTime childNewEnd = this.childSessions[i].getEndDate().toLocalDateTime();
                LocalDateTime childOldStart = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.childSessions[i].getLength()), TimeZone.getDefault().toZoneId());
                LocalDateTime childOldEnd = childOldStart.plusSeconds(Duration.between(childNewStart, childNewEnd).getSeconds());
                occurrences.add(new Occurrence(childNewStart, childNewEnd));
                occurrences.removeIf(o -> o.equals(new Occurrence(childOldStart, childOldEnd)));
            }
        }

        return occurrences.toArray(new Occurrence[0]);

    }
	
	/**
     * Overloaded method of getOccurences which only return occurrences between 2 dates
     *
     * @param max   max number of occurences to return
     * @param start start Date of your time window
     * @param end end Date of your time window
     * @return
     */
    public Occurrence[] getOccurrences(int max, LocalDate start, LocalDate end){
        Occurrence[] occurrences = getOccurrences(max);
        ArrayList<Occurrence> ALoccur = new ArrayList<>();
        for(int x = 0; x < occurrences.length; x++){

            if((start.getDayOfYear() <= occurrences[x].getStartDate().getDayOfYear())
                    && (end.getDayOfYear() > occurrences[x].getStartDate().getDayOfYear())){
                    ALoccur.add(occurrences[x]);
                }
            }
        return ALoccur.toArray(new Occurrence[0]);
    }

    /**
     * Save the course session's details to the database.
     * @return true if successful, false otherwise.
     */
    public boolean save(){
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // don't need to save to database, there have been no changes
        if(this.recordSaved){
            return true;
        }
        // if the record was created successfully in the database (on a previous call to save(), but was unable to retrieve the sessionID thereafter), then cannot save
        if(this.recordExists && this.sessionID == null){
            return false;
        }
        // prepare query statement
        String sql;
        // if the record does not exist in the database, then we must execute an insert query (otherwise an update query)
        if(!this.recordExists){
            sql = "INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours) "
                  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }else{
            sql = "UPDATE courseSession SET sessionPID = ?, courseID = ?, sessionType = ?, startDate = ?, endDate = ?, length = ?, "
                  + " recType = ?, priority = ?, weighting = ?, studyHours = ? WHERE sessionID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[10];
            types = new int[10];
        }else{
            params = new Object[11];
            types = new int[11];
            params[10] = this.sessionID;
            types[10] = Types.INTEGER;
        }
        params[0] = this.sessionPID;
        types[0] = Types.INTEGER;
        params[1] = this.courseID;
        types[1] = Types.INTEGER;
        params[2] = this.type;
        types[2] = Types.VARCHAR;
        params[3] = this.startDate;
        types[3] = Types.TIMESTAMP;
        params[4] = this.endDate;
        types[4] = Types.TIMESTAMP;
        params[5] = this.length;
        types[5] = Types.BIGINT;
        params[6] = this.recType;
        types[6] = Types.VARCHAR;
        params[7] = this.priority;
        types[7] = Types.INTEGER;
        params[8] = this.weighting;
        types[8] = Types.DOUBLE;
        params[9] = this.studyHours;
        types[9] = Types.INTEGER;
        // execute query
        if(Database.update(sql, params, types)){
            // get session ID
            if(this.length == null){
                sql = "SELECT sessionID FROM courseSession WHERE courseID = ? AND sessionType = ? AND startDate = ? AND endDate = ? AND length IS NULL";
            }else {
                sql = "SELECT sessionID FROM courseSession WHERE courseID = ? AND sessionType = ? AND startDate = ? AND endDate = ? AND length = ?";
            }
            if(this.length == null) {
                params = new Object[4];
                types = new int[4];
            }else{
                params = new Object[5];
                types = new int[5];
            }
            params[0] = this.courseID;
            types[0] = Types.INTEGER;
            params[1] = this.type;
            types[1] = Types.VARCHAR;
            params[2] = this.startDate;
            types[2] = Types.TIMESTAMP;
            params[3] = this.endDate;
            types[3] = Types.TIMESTAMP;
            if(this.length != null) {
                params[4] = this.length;
                types[4] = Types.BIGINT;
            }
            ResultSet rs = Database.query(sql, params, types); // if fetching the sessionID fails, this object will no longer be able to save data to the database (i.e. save() will always return false)
            try {
                if (rs.first()) {
                    this.sessionID = rs.getInt("sessionID");
                }
                this.loadChildSessions(); // once the sessionID has been fetched we can now run loadChildSessions()
            }catch (Exception e){}
            this.recordExists = true;
            this.recordSaved = true;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Delete the course session's details from the database.
     * @return true if successful, false otherwise.
     */
    public boolean delete() {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        // delete associated study sessions (i.e. study sessions that were created for this event)
        String sql = "DELETE FROM studySession WHERE courseSessionID = ?";
        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = this.sessionID;
        types[0] = Types.INTEGER;
        if(!Database.update(sql, params, types)){
            return false;
        }
        // delete associated child course sessions (if this is a parent course session for a recurring series)
        if (this.recType != null && this.recType != "none") {
            for(int i = 0 ; i < this.childSessions.length ; i++){
                if(!this.childSessions[i].delete()){
                    return false;
                }
            }
        }
        // finally, delete the course session itself
        sql = "DELETE FROM courseSession WHERE sessionID = ?";
        params = new Object[1];
        types = new int[1];
        params[0] = this.sessionID;
        types[0] = Types.INTEGER;
        if(Database.update(sql, params, types)) {
            return true;
        } else {
            System.out.println("Failed to delete course session for courseSessionID: " + this.sessionID + ".");
            return false;
        }
    }

}
