package com.stms.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {
        try{
            user1 = new User("scott.hallauer@gmail.com");
            user2 = new User("EVRJON003@myuct.ac.za");
            user3 = new User("BRNJES018@myuct.ac.za");
        } catch (Exception e){
            System.out.println("Failed User Test creation. Tests invalid");
            e.printStackTrace();
        }
    }

    @Test
    void getSemesters() {
        Semester[] semesters = user1.getSemesters();
        assertEquals(2, semesters.length);
    }

    @Test
    void getTasks() {
    }

    @Test
    void getUserID() {
        int x = user1.getUserID();
        assertEquals(1, x);
    }

    @Test
    void getFirstName() {
        String s = user2.getFirstName();
        assertEquals(s, "Jonathon");
    }

    @Test
    void setFirstName() {
        user2.setFirstName("Steve");
        String s = user2.getFirstName();
        assertEquals("Steve", s);
        user2.setFirstName("Jonathon");
        s = user2.getFirstName();
        assertEquals(s, "Jonathon");
    }

    @Test
    void getLastName() {
        String s = user3.getLastName();
        assertEquals("Bourn", s);
    }

    @Test
    void setLastName() {
        user3.setLastName("JeanB");
        assertEquals("JeanB", user3.getLastName());
        user3.setLastName("Bourn");
    }

    @Test
    void getInitials() {
        String s1 = user2.getInitials();
        String s2 = user1.getInitials();
        assertEquals("JE", s1);
        assertEquals("SH", s2);
        assertNotEquals(s1,s2);
    }

    @Test
    void getEmail() {
        String s = user2.getEmail();
        assertEquals("EVRJON003@myuct.ac.za", s);
    }

    @Test
    void setEmail() {
        user1.setEmail("HLLSCO002@myuct.ac.za");
        String s = user1.getEmail();
        user1.setEmail("scott.hallauer@gmail.com");
        assertEquals("HLLSCO002@myuct.ac.za", s);
    }

    @Test
    void checkPassword() {
        assertTrue(user1.checkPassword("password"));
        assertTrue(user2.checkPassword("12345678"));
    }

    @Test
    void setPassword() {
        user3.setPassword("ThisisApassword");
        assertTrue(user3.checkPassword("ThisisApassword"));
        user3.setPassword("p3gasus");
    }

    @Test
    void save() {
        User user = new User();
        user.setPassword("12345678");
        user.setEmail("123Test@Test.test.test");
        user.setFirstName("Testy");
        user.setLastName("McTester");
        assertTrue(user.save());
    }
}