package com.stms.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

public class DateUtil {
    DateUtil(){

    }

    //public int daysUntil(Date date){
        //Date today = getDateToday();
   // }
    //If someone knows better solution please help
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


    public LocalDate getDateToday(){
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate toReturn = LocalDate.now();
        System.out.println(toReturn.toString());
        return toReturn;
    }
}
