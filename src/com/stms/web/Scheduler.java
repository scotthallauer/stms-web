package com.stms.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Scheduler {

    private int UserID;
    private boolean timeTable[][];
    private int DaysTilDue;
    private int SemesterID;
    private int toBed;
    private int toWake;


    /**
     * Constructors for the class. One that is blank and another that passes the UserID to the class.
     * The userID is necessary for the class to execute but also has a set method to access it.
     */

    Scheduler(){
        toBed = 0;
        toWake = 8;
    }

    Scheduler(int UserID){
        this.UserID = UserID;
        toBed = 0;
        toWake = 8;
    }

    /**
     * This method will generate sessions to cover the amount of desired studyTime including a revision session
     * The day before and then will return an integer of the amount of hours
     *
     * @param numOfHours Number of hours that the user wants to work
     * @param dueDate The Date that the work must be completed by
     * @return Amount of hours created or negative if no session created
     */
    public int generateSessions(int numOfHours, LocalDate dueDate) {
        DaysTilDue = dueDate.getDayOfYear() - Utilities.getDateToday().getDayOfYear();
        if (Utilities.getDateToday().isAfter(dueDate)) {
            System.out.println("Due date has passed.");
            return -1;
        }
        int avghoursperDay = Math.round(numOfHours / DaysTilDue) + 2;

        timeTable = new boolean[DaysTilDue][24];
        // where timeTable[day][hour]
        for (int x = 0; x<DaysTilDue;x++){
            for(int y = 0; y < 24; y++){
                if (y < toWake){ //Can edit for sleep arrangements
                    timeTable[x][y] = false;
                } else {//free time before checking scheduled sessions
                    timeTable[x][y] = true;
                }
            }
        }

        //Major variables have all be intialized

        String sql = "SELECT sessionID \n" +
                "    FROM courseSession\n" +
                "    INNER JOIN\n" +
                "    (\n" +
                "        SELECT courseID\n" +
                "        FROM course\n" +
                "        INNER JOIN semester\n" +
                "        ON course.semesterID1 = semester.semesterID\n" +
                "        WHERE \n" +
                "        semester.userID = ?\n" +
                "    ) AS C\n" +
                "    ON courseSession.courseID = C.courseID" +
                "    ORDER BY startDate DESC;";

        Object[] params = new Object[1];
        int[] types = new int[1];
        params[0] = UserID;
        types[0] = Types.INTEGER;
        ResultSet rs = Database.query(sql, params, types);
        int courseID;
        boolean flag = true;
        //Data is now loaded into the resultSet
        try{
            CourseSession courseSession;
            while(rs.next()){
                int sessionID = rs.getInt("sessionID");
                try{
                    courseSession = new CourseSession(sessionID);
                    if (flag){
                        courseID = courseSession.getCourseID();
                        params[0] = courseID;
                        flag = false;
                    }
                    Occurrence[] occurrences = courseSession.getOccurrences(1000, Utilities.getDateToday(), dueDate);
                    for(int x = 0; x < occurrences.length; x++){
                        if (occurrences[x] != null){
                            int start = occurrences[x].getStartDate().getHour();
                            int end = occurrences[x].getEndDate().getHour() + 1;
                            int dayCount = occurrences[x].getStartDate().getDayOfYear();
                            dayCount = dayCount - Utilities.getDateToday().getDayOfYear();
                            for(int m = start; m < end; m++){
                                timeTable[dayCount][m] = false;
                            }
                        }
                    }
                } catch (Exception e){
                    System.out.println("Failed to create CourseSession object");
                    e.printStackTrace();
                }


            }
        } catch (SQLException e){
            e.printStackTrace();
        } //This works for all course sessions generated from DHTLMX

        sql = "SELECT semesterID1 FROM course WHERE courseID = ?;";

        ResultSet rs2;
        try {
            rs = Database.query(sql, params, types);
            if (rs.next()){
                SemesterID = rs.getInt("semesterID1");
            }
            //FIX THE SEMESTERID GENERATION HERE COULD BE INVALID SEMESTER
            if(rs != null) {
                while (rs.next()) {
                    //SemesterID = rs.getInt("semesterID");
                    sql = "SELECT startTime, endTime FROM studySession WHERE semesterID = ?;";
                    params[0] = SemesterID;
                    rs2 = Database.query(sql, params, types);

                    while (rs2.next()) {
                        LocalDateTime start = rs2.getTimestamp("startTime").toLocalDateTime();
                        LocalDateTime end = rs2.getTimestamp("endTime").toLocalDateTime();
                        ScheduleStudySessions(start, end);
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        //END OF BUILD PHASE
        //Start of burn phase

        int hourCount = 0;
        int fullCount = 0;
        for (int x = DaysTilDue -1; x > -1; x--){
            //loop through hours in a day
            for (int y = 0; y < 24; y++){
                if((timeTable[x][y]) && (hourCount < avghoursperDay) && (fullCount + hourCount < numOfHours)){
                    //Create new studysession
                    LocalDate due = Utilities.getDateToday().plusDays(x);
                    LocalDateTime startTime = due.atTime(y, 0);
                    LocalDateTime endTime;
                    if (y == 23){
                        endTime = due.atTime(0, 0);
                    }else {
                        endTime = due.atTime(y+1, 0);
                    }
                    Timestamp timestamp = Timestamp.valueOf(startTime);
                    StudySession toSchedule = new StudySession();
                    toSchedule.setStartTime(timestamp);
                    timestamp = Timestamp.valueOf(endTime);

                    toSchedule.setEndTime(timestamp);
                    toSchedule.setSemesterID(SemesterID);
                    toSchedule.setConfirmed(true);
                    toSchedule.setNote("Auto generated study session.");
                    //Add some form of check for duplicate record
                    flag = toSchedule.save();
                    if(!flag){
                        System.out.println("Failed to save session");
                    }
                    hourCount += 1;
                }

            }
            fullCount += hourCount;
            hourCount = 0;
        }
        return numOfHours - fullCount;
    }

    /**
     * The below method only works with the DHTLMX recurring fields and
     * this one is for previously scheduled studySessions
     *
     * @param startTime Beginning of the studySession
     * @param endTime End of the studySession
     */
    private void ScheduleStudySessions(LocalDateTime  startTime, LocalDateTime endTime){
        int dayNum = startTime.getDayOfYear() - Utilities.getDateToday().getDayOfYear();
        if(dayNum >= 0){
            for (int x = startTime.getHour(); x < endTime.getHour(); x++){
                timeTable[dayNum][x] = false;
            }
            if (startTime.getHour() == 23){
                timeTable[dayNum][23] = false;
            }
        } else {
            System.out.println("Study session has already been completed");
        }
    }


    public void setUserID(int UserID){
        this.UserID = UserID;
    }

    public int getUserID(){
        return UserID;
    }

    public  void setBedTime(int bedTime){
        toBed = bedTime;
    }

    public void setWakeTime(int wake){
        toWake = wake;
    }
}



