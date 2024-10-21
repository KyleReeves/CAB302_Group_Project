package org.example.cab302_project;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Data Access Object class for managing the user operations in the database which is inclusive of the registration
 * and authentication as well as password management
 *
 */
public class UserDAO {
    private Connection connection;

    /**
     * Constructs The UserDao and initializes the database connection
     *
     */
    public UserDAO() {
        try {
            connection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            System.err.println("Error establishing database connection in UserDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a users table ion the database if it does not yet exist
     * this table stores IDs, usernames and password hashes
     */
    public void createUserTable() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " username TEXT NOT NULL UNIQUE, " +
                    " password_hash TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating Users table: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * registers a new user in the database which comprises a username and password
     *
     * @param username the users username
     * @param password the plain text password for a user
     * @return true if the registration was successful or false otherwise
     */
    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO Users(username, password_hash) VALUES(?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Authneticates a user by checking if the username and password match what is within the database
     *
     * @param username the username that is attempting a login
     * @param password the passowrd that is attempted with the login
     * @return true if the login was sucessful and false if credentials are incorrect
     */
    public User loginUser(String username, String password) {
        String sql = "SELECT id, username, password_hash FROM Users WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (verifyPassword(password, storedHash)) {
                    return new User(rs.getInt("id"), rs.getString("username"), storedHash);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Hashes a password with the SHA-256 algorithm to ensure it cant be read in plain text
     *
     * @param password the plain text hash
     * @return the hashed password in a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies if the password in plain text matches the stored hashed password
     *
     * @param password The plain text password which is used to verify
     * @param storedHash the stored password hash which will be compared against the password attempted
     * @return true if the password matches and false otherwise
     */
    private boolean verifyPassword(String password, String storedHash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(storedHash);
    }
}