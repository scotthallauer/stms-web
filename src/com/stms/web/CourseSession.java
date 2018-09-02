package com.stms.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.*;

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

    private Integer sessionID;
    private Integer sessionPID;
    private Integer courseID;
    private String type;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long length;
    private String recType;
    private String location;
    private String note;
    private Integer priority;
    private Double weighting;
    private Double possibleMark;
    private Double earnedMark;

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
            this.location = rs.getString("location");
            this.note = rs.getString("note");
            this.priority = rs.getInt("priority");
            if(rs.wasNull()){
                this.priority = null;
            }
            this.weighting = rs.getDouble("weighting");
            if(rs.wasNull()){
                this.weighting = null;
            }
            this.possibleMark = rs.getDouble("possibleMark");
            if(rs.wasNull()){
                this.possibleMark = null;
            }
            this.earnedMark = rs.getDouble("earnedMark");
            if(rs.wasNull()){
                this.earnedMark = null;
            }
            this.recordExists = true;
            this.recordSaved = true;
        }else{
            throw new NullPointerException("No CourseSession exists with the sessionID " + sessionID);
        }
    }

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

    public void setLocation(String location) {
        this.location = location;
        this.recordSaved = false;
    }

    public String getLocation() {
        return this.location;
    }

    public void setNote(String note) {
        this.note = note;
        this.recordSaved = false;
    }

    public String getNote() {
        return this.note;
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
        return this.weighting;
    }

    public void setPossibleMark(double possibleMark) {
        this.possibleMark = possibleMark;
        this.recordSaved = false;
    }

    public Double getPossibleMark() {
        return this.possibleMark;
    }

    public void setEarnedMark(double earnedMark) {
        this.earnedMark = earnedMark;
        this.recordSaved = false;
    }

    public Double getEarnedMark() {
        return this.earnedMark;
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

                // add first occurrence
                period = new Occurrence(startDate, startDate.plusSeconds(this.length));
                occurrences.add(period);

                // add all other occurrences
                boolean flag = false;
                for(int i = 1 ; i < max && !flag ; i++){
                    for(int j = 1 ; j < days.length && i < max && !flag ; j++){
                        LocalDateTime start = period.getStartDate().plusDays(days[j]-days[j-1]);
                        LocalDateTime end = start.plusSeconds(this.length);
                        if(end.isBefore(endDate) || end.isEqual(endDate)) {
                            period = new Occurrence(start, end);
                            occurrences.add(period);
                            i++;
                        }else{
                            flag = true;
                        }
                    }
                    LocalDateTime start = period.getStartDate().minusDays(days[days.length-1]-days[0]).plusWeeks(count);
                    LocalDateTime end = start.plusSeconds(this.length);
                    if(end.isBefore(endDate) || end.isEqual(endDate)) {
                        period = new Occurrence(start, end);
                        occurrences.add(period);
                    }else{
                        flag = true;
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
        Occurrence[] toRe = new Occurrence[ALoccur.size()];
        for (int x = 0; x < ALoccur.size();x++){
            toRe[x] = ALoccur.get(x);

        }
        return toRe;
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
            sql = "INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, location, note, priority, weighting, possibleMark, earnedMark) "
                  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }else{
            sql = "UPDATE courseSession SET sessionPID = ?, courseID = ?, sessionType = ?, startDate = ?, endDate = ?, length = ?, "
                  + " recType = ?, location = ?, note = ?, priority = ?, weighting = ?, possibleMark = ?, earnedMark = ? WHERE sessionID = ?";
        }
        // prepare query parameters
        Object[] params;
        int[] types;
        if(!this.recordExists){
            params = new Object[13];
            types = new int[13];
        }else{
            params = new Object[14];
            types = new int[14];
            params[13] = this.sessionID;
            types[13] = Types.INTEGER;
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
        params[7] = this.location;
        types[7] = Types.VARCHAR;
        params[8] = this.note;
        types[8] = Types.VARCHAR;
        params[9] = this.priority;
        types[9] = Types.INTEGER;
        params[10] = this.weighting;
        types[10] = Types.DOUBLE;
        params[11] = this.possibleMark;
        types[11] = Types.DOUBLE;
        params[12] = this.earnedMark;
        types[12] = Types.DOUBLE;
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
    private boolean deleteCourseSession () {
        // check if database is connected
        if(!Database.isConnected()) {
            return false;
        }
        String sql = "DELETE FROM courseSession WHERE sessionID = ?";
        Object[] params;
        int[] types;
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
