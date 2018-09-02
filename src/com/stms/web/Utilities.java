package com.stms.web;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * Translate the day you get from a calendar instance into a char character corresponding to the day where monday =1
     * and sunday = 7. 0 is invalid input
     *
     * @param dayPrefix Prefix of the day from the calendar object e.g. mon
     * @return Corresponding char in numeric form for the prefix
     */
    public static int dayToInt(String dayPrefix){
        if (dayPrefix.equals("Mon")){
            return 1;
        } else if (dayPrefix.equals("Tue")){
            return 2;
        } else if (dayPrefix.equals("Wed")){
            return 3;
        } else if (dayPrefix.equals("Thu")){
            return 4;
        } else if (dayPrefix.equals("Fri")){
            return 5;
        } else if (dayPrefix.equals("Sat")){
            return 6;
        } else if (dayPrefix.equals("Sun")){
            return 7;
        } else{
            System.out.println("Invalid input " + dayPrefix + ".");
            return '0';
        }
    }

    /**
     * Takes the date in the form of LocalDate and returns the number of the day in the year
     *
     * @param date LocalDate you want the DayNum for
     * @return The number of the day in the year corresponding to input date
     */
    public static int calcDayNumInYear(LocalDate date){
        return date.getDayOfYear();
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
