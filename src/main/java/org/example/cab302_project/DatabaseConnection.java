package org.example.cab302_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class manages the database connection and ensures that there is only one instance
 * of the database connection being used throughout the application
 */
public class DatabaseConnection {

    /**
     * The instance of the database connection
     */
    private static Connection instance = null;

    /**
     * The URL for the SQLite database
     */
    private static final String URL = "jdbc:sqlite:database.db";

    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConnection() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Returns the single instance of the database connection. If it doesn't exist or is closed, initialize a new connection
     *
     * @return the instance of the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL);
        }
        return instance;
    }

    /**
     * Closes the current database connection if it exists and is open
     *
     * @throws SQLException if a database access error occurs
     */
    public static void closeConnection() throws SQLException {
        if (instance != null && !instance.isClosed()) {
            instance.close();
            instance = null;
        }
    }

    /**
     * This method sets a connection for the primary purpose of testing
     *
     * @param testConnection the test connection to be set
     */
    public static void setTestInstance(Connection testConnection) {
        instance = testConnection;
    }

    /**
     * Resets the connection instance to null. Useful for testing and ensuring a fresh connection.
     */
    public static void resetConnection() {
        instance = null;
    }
}