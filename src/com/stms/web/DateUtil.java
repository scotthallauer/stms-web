package com.stms.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

public class DateUtil {

    private int year;

    DateUtil(){

    }

    public int daysUntil(LocalDate date){
        LocalDate today = getDateToday();
        int sum = calcDayNumInYear(date);
        sum -= calcDayNumInYear(today);
        if (sum > 0){
            return  sum;
        } else {
            return -1;
        }
    }

    /**
     * Takes the date in the form of LocalDate and returns the number of the day in the year
     *
     * @param date1 LocalDate you want the DayNum for
     * @return The number of the day in the year corresponding to input date
     */
    public int calcDayNumInYear(LocalDate date1){
        //Date1 is due date and date 2 is
        //date in format YYYY-MM-DD
        String s = date1.toString();
        int year = Integer.parseInt(s.substring(0,4));
        int month = Integer.parseInt(s.substring(5,7));
        int day = Integer.parseInt(s.substring(8,10));
        int total = 0;
        if (month > 1){
            total += 31;
        }
        if (month > 2){
            total += 28;
            if (year % 4 == 0){
                total += 1;
            }
        }
        if (month > 3){
            total += 31;
        }
        if (month > 4){
            total += 30;
        }
        if (month > 5){
            total += 31;
        }if (month > 6){
            total += 30;
        }
        if (month > 7){
            total += 31;
        }
        if (month > 8){
            total += 31;
        }
        if (month > 9){
            total += 30;
        }
        if (month > 10){
            total += 31;
        }
        if (month > 11){
            total += 30;
        }
        total += day;
        return total;
    }

    /**
     * Takes in the Day number in the year as a parameter and then outputs a LocalDate of the corresponding date
     *
     * @param DayNum The day number in the year supposed to be retrieved from the method above
     * @return The Date in a LocalDate variable
     */
    public LocalDate CalcDateFromDayNum(int DayNum){
        LocalDate toRe;
        if (DayNum <= 31){
            toRe = LocalDate.of(2018,1,DayNum);
        } else if (DayNum <= 59){
            toRe = LocalDate.of(2018,1,59 - DayNum);
        } else if (DayNum <= 90){
            toRe = LocalDate.of(2018,1,90 - DayNum);
        } else if (DayNum <= 120){
            toRe = LocalDate.of(2018,1,120 - DayNum);
        } else if (DayNum <= 151){
            toRe = LocalDate.of(2018,1,151 - DayNum);
        } else if (DayNum <= 181){
            toRe = LocalDate.of(2018,1,181 - DayNum);
        } else if (DayNum <= 212){
            toRe = LocalDate.of(2018,1,212- DayNum);
        } else if (DayNum <= 243){
            toRe = LocalDate.of(2018,1,243 - DayNum);
        } else if (DayNum <= 273){
            toRe = LocalDate.of(2018,1,273 - DayNum);
        } else if (DayNum <= 304){
            toRe = LocalDate.of(2018,1,304 - DayNum);
        } else if (DayNum <= 335){
            toRe = LocalDate.of(2018,1,335 - DayNum);
        } else if (DayNum <= 365){
            toRe = LocalDate.of(2018,1,365 - DayNum);
        } else {
            System.out.println("Error invalid day number has been entered");
            throw new IndexOutOfBoundsException();
        }
        return toRe;
    }

    /**
     * Getters and setters for this class
     */
    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return this.year;
    }

    /**
     * Gets and returns the current date in the form of LocalDate
     *
     * @return Date today
     */
    public LocalDate getDateToday(){
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate toReturn = LocalDate.now();
        System.out.println(toReturn.toString());
        return toReturn;
    }
}

