package web.stms.com;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class Scheduler {

    private Utilities Util;
    private int UserID;
    private Database DB;
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
        Util = new Utilities();
        toBed = 0;
        toWake = 8;
    }

    Scheduler(int UserID){
        this.UserID = UserID;
        Util = new Utilities();
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
        DaysTilDue = Util.calcDayNumInYear(dueDate) - Util.calcDayNumInYear(Util.getDateToday());
        if (Util.getDateToday().isAfter(dueDate)) {
            System.out.println("Due date has passed you chop nugget");
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

        String sql = "SELECT startDate, endDate, recType, length\n" +
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
        ResultSet rs = DB.query(sql, params, types);
        //Data is now loaded into the resultSet
        try{
            while (rs.next()) {
                LocalDateTime timeSlot = rs.getTimestamp("startDate").toLocalDateTime();
                LocalDateTime endDate = rs.getTimestamp("endDate").toLocalDateTime();
                int length = rs.getInt("length");
                String recType = rs.getString("recType");
                scheduleEvent(timeSlot, endDate,recType, length);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } //This works for all course sessions generated from DHTLMX

        sql = "SELECT semesterID FROM semester WHERE userID = ?;";

        try {
            rs = DB.query(sql, params, types);
            if (rs.next()){
                SemesterID = rs.getInt("semesterID");
            }
            //FIX THE SEMESTERID GENERATION HERE COULD BE INVALID SEMESTER
        } catch (SQLException e){
            e.printStackTrace();
        }

        ResultSet rs2;
        try{
            if(rs != null) {
                while (rs.next()) {
                    //SemesterID = rs.getInt("semesterID");
                    sql = "SELECT startTime, endTime FROM studySession WHERE semesterID = ?;";
                    params[0] = SemesterID;
                    rs2 = DB.query(sql, params, types);

                    while (rs2.next()) {
                        LocalDateTime start = rs2.getTimestamp("startTime").toLocalDateTime();
                        LocalDateTime end = rs2.getTimestamp("endTime").toLocalDateTime();
                        ScheduleStudySessions(start, end);
                        }
                    }
                }
        } catch (SQLException e){
            System.out.println("Fail on studySessions try catch");
            e.printStackTrace();
        }

        for (int x = 0; x < DaysTilDue; x++){
            System.out.print("Day number " + x + " : ");
            for (int y = 0; y < 24; y++){
                System.out.print( timeTable[x][y] + " ");
            }
            System.out.print('\n');
        }

        int hourCount = 0;
        int fullCount = 0;
        for (int x = DaysTilDue -1; x > -1; x--){
            //loop through hours in a day
            for (int y = 0; y < 24; y++){
                if((timeTable[x][y]) && (hourCount < avghoursperDay) && (fullCount + hourCount < numOfHours)){
                    //Create new studysession
                    LocalDate due = Util.CalcDateFromDayNum(Util.calcDayNumInYear(Util.getDateToday()) + x);
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
                    System.out.println(timestamp.toString() + " Testing");
                    toSchedule.setEndTime(timestamp);
                    toSchedule.setSemesterID(SemesterID);
                    toSchedule.setConfirmed(true);
                    toSchedule.setNote("Auto generated study session.");
                    //Add some form of check for duplicate record
                    boolean flag = toSchedule.save();
                    if(!flag){
                        System.out.println("Failed to save session");
                    }
                    hourCount += 1;
                    System.out.println("Study Session created on day " + x + " starting at hour " + y);
                }

            }
            fullCount += hourCount;
            hourCount = 0;
        }
        System.out.println(fullCount);

        return numOfHours - fullCount;
    }

    /**
     * The below method only works with the DHTLMX recurring fields and
     * this one is for previously scheduled studySessions
     *
     * @param startTime Beginning of the studySession
     * @param endTime End of the studySession
     */
    public void ScheduleStudySessions(LocalDateTime  startTime, LocalDateTime endTime){
        int dayNum = Util.calcDayNumInYear(startTime.toLocalDate()) - Util.calcDayNumInYear(Util.getDateToday());
        if(dayNum >= 0){
            for (int x = startTime.getHour(); x < endTime.getHour(); x++){
                timeTable[dayNum][x] = false;
            }
        } else {
            System.out.println("Study session has already been completed");
            //Find a better fix
        }
    }

    /**
     * The point of this method is to set all used time on the schedule to false
     *
     * @param startTime beginning time and date of event
     * @param endTime final date of event
     * @param recType DHTMLX recurring String
     * @param length length of event in seconds
     */
    private void scheduleEvent(LocalDateTime startTime, LocalDateTime endTime, String recType, int length){
        int count = 0;
        while (length > 0){
            length -= 3600;
            count += 1;
        }
        int startHour = startTime.getHour();
        System.out.println(startHour + " where the start time is " + startTime.toString() + " and the end time is " + endTime.toString());
        Calendar cal = Calendar.getInstance();
        String s = cal.getTime().toString();
        s = s.substring(0, 3);
        recType = recType.substring(recType.indexOf(',')-1);

        int DayNumWeek = dayToInt(s);
        for(int x = 0; x < DaysTilDue; x++){
            String s1  = "" + DayNumWeek;
            if(recType.indexOf(s1) != -1){
                if((Util.calcDayNumInYear(startTime.toLocalDate()) < Util.calcDayNumInYear(Util.getDateToday()) + x)
                        && (Util.calcDayNumInYear(endTime.toLocalDate()) > Util.calcDayNumInYear(Util.getDateToday()) + x)){
                    for (int y = startHour; y < count + startHour; y++){
                        timeTable[x][y] = false;
                        System.out.println("I have set day " + x + " to false and also hour " + y);
                    }
                }
            }
            DayNumWeek += 1;
            if (DayNumWeek == 8){
                DayNumWeek = 1;
            }
        }
    }


    /**
     * This method takes the end time of one session nd the start time of the next to calculate the free time between
     *
     * @param endTime End time of current session
     * @param startTime start time of next session
     * @return number of hours that are free. -1 if nothing is free
     */
    public int calcFreeTime(LocalTime endTime, LocalTime startTime){
        if (endTime.isAfter(startTime)){
            System.out.println("Error. Start time is before endTime");
            return -1;
        }
        int hours1 = endTime.getHour();
        int minute1 = endTime.getMinute();
        int hours2 = startTime.getHour();
        int minute2 = startTime.getMinute();
        int time = hours2 - hours1;

        if((time <= 1) && (minute1 < minute2 )){
            return -1;
        }

        if (minute1 > minute2){

        } else {

        }


        System.out.println("The difference between  " + endTime.toString() + " and  " + startTime.toString() + " is " + time);
        return time;
    }


    /**
     * Translate the day you get from a calendar instance into a char character corresponding to the day where monday =1
     * and sunday = 7. 0 is invalid input
     *
     * @param dayPrefix Prefix of the day from the calendar object e.g. mon
     * @return Corresponding char in numeric form for the prefix
     */
    public int dayToInt(String dayPrefix){
        if (dayPrefix.equals("Mon")){
            return 1;
        } else if (dayPrefix.equals("Tue")){
            return 2;
        } else if (dayPrefix.equals("Wed")){
            return 3;
        } else if (dayPrefix.equals("Thur")){
            return 4;
        } else if (dayPrefix.equals("Fri")){
            return 5;
        } else if (dayPrefix.equals("Sat")){
            return 6;
        } else if (dayPrefix.equals("Sun")){
            return 7;
        } else{
            System.out.println("Invalid input");
            return '0';
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
