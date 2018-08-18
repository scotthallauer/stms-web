package com.stms.web;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;

public class Utilities {

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

}
