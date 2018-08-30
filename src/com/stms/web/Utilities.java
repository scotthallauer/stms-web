package com.stms.web;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

public class Utilities {

   Utilities(){
   
   }

    public static boolean validateEmail(String email){
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }

    /**
     * This method is used to hash a password that the user inputs
     * It is used in both create account and log in use case.
     * Uses SHA - 256 hashing to hash a password
     *
     * @param plaintext password that will be hashed
     * @param salt Salt for the user gotten from the DB
     * @return Hashed password including salt
     */
    public static String hashPassword(String plaintext, String salt) throws Exception {
        // Algorithm reference: https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
        String intermediate = plaintext + salt;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = messageDigest.digest(intermediate.getBytes("UTF-8"));
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }

    /**
     * Used to create a random string of a specific length (can be used for generating a salt for password hashing)
     * @param length the number of characters in the output string
     * @return the random generated string
     */
    public static String randomString(int length){
        // Algorithm reference: https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
        if(length < 1){
            return "";
        }
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String salt = "";
        for(int i = 0 ; i < length ; i++){
            salt += symbols.charAt(random.nextInt(symbols.length()));
        }
        return salt;
    }

    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }
    
    public static int daysUntil(LocalDate date){
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
     * This method checks to see whether or not the DHTLMX Has scheduled a session in said time period
     *
     * @param rrule The DHTMLX repeating String
     * @param day The integer value of the checking day Mon = 1 and Sun = 7
     * @return true if not scheduled on said day else false
     */

    public static boolean checkDHT(String rrule, char day){
        rrule = rrule.substring(rrule.indexOf(',') - 1);
        System.out.println(rrule);
        if (rrule.indexOf(day) != -1){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes the date in the form of LocalDate and returns the number of the day in the year
     *
     * @param date1 LocalDate you want the DayNum for
     * @return The number of the day in the year corresponding to input date
     */
    public static int calcDayNumInYear(LocalDate date1){
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
    public static LocalDate CalcDateFromDayNum(int DayNum){
        LocalDate toRe;
        if (DayNum <= 31){
            toRe = LocalDate.of(2018,1,DayNum);
        } else if (DayNum <= 59){
            toRe = LocalDate.of(2018,2,DayNum - 31);
        } else if (DayNum <= 90){
            toRe = LocalDate.of(2018,3,DayNum - 59);
        } else if (DayNum <= 120){
            toRe = LocalDate.of(2018,4,DayNum - 90);
        } else if (DayNum <= 151){
            toRe = LocalDate.of(2018,5,DayNum - 120);
        } else if (DayNum <= 181){
            toRe = LocalDate.of(2018,6,DayNum - 151);
        } else if (DayNum <= 212){
            toRe = LocalDate.of(2018,7,DayNum - 181);
        } else if (DayNum <= 243){
            toRe = LocalDate.of(2018,8,DayNum - 212);
        } else if (DayNum <= 273){
            toRe = LocalDate.of(2018,9,DayNum - 243);
        } else if (DayNum <= 304){
            toRe = LocalDate.of(2018,10,DayNum - 273);
        } else if (DayNum <= 335){
            toRe = LocalDate.of(2018,11,DayNum - 304);
        } else if (DayNum <= 365){
            toRe = LocalDate.of(2018,12,DayNum - 335);
        } else {
            System.out.println("Error invalid day number has been entered");
            throw new IndexOutOfBoundsException();
        }
        return toRe;
    }

    /**
     * Gets and returns the current date in the form of LocalDate
     *
     * @return Date today
     */
    public static LocalDate getDateToday(){
        return LocalDate.now();
    }

    public static String capitalise(String str) {
        String[] arr = str.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

}
