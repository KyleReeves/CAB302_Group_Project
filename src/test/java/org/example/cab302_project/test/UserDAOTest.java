package org.example.cab302_project.test;

import org.example.cab302_project.DatabaseConnection;
import org.example.cab302_project.User;
import org.example.cab302_project.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for testing the functionality of the UserDAO class
 * Ensuring that user registration and login behaviour works correctly
 */
class UserDAOTest {

    private UserDAO userDAO;

    /**
     * Sets up the test environment by initializing the userDAO instance
     * also creates user tables within the test database
     */
    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();

        userDAO.createUserTable();

        clearTable();
    }

    /**
     * Helper method which clears a users table in the database to ensure that there isn't any prior to running
     * tests
     */
    private void clearTable() {
        try {
            Connection connection = DatabaseConnection.getInstance();

            connection.createStatement().execute("DELETE FROM Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the registration and retrieval of a user from the database, ensuring that a user is able to successfully register
     * and is able to be retrieved with the correct user and password inputted
     */
    @Test
    void testRegisterAndGetUser() {

        boolean registered = userDAO.registerUser("testUser", "password123");
        assertTrue(registered, "User should be registered successfully");
        System.out.println("User had a successful registration ");

        User user = userDAO.loginUser("testUser", "password123");
        assertNotNull(user, "The user should be found in the database");
        System.out.println("User found in the database after registration");

        assertEquals("testUser", user.getUsername(), "Username should match the registered one");
    }

    /**
     * Tests the login failure cases if an incorrect password is used
     * Makes sure that an incorrect password means a user is unable to login
     */
    @Test
    void testFailedLoginWithWrongPassword() {

        userDAO.registerUser("testUser", "password123");

        // try logging in with an incorrect password
        User user = userDAO.loginUser("testUser", "wrongPassword");
        assertNull(user, "User should not be able to be logged in with an incorrect password");

        System.out.println("User was unable to login with incorrect password");
    }
}