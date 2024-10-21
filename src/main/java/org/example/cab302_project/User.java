package org.example.cab302_project;

/**
 * represents a user in the system with an ID, username and password hash
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;

    /**
     * Constructs a User object with the specified id, username and password hash
     *
     * @param id the ID of the user
     * @param username the username of the user
     * @param passwordHash the hashed password of a user
     */
    public User(int id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Constructs a User object without a specified ID
     * The ID is set to -1 by default which indicates that a user has not yet been assigned an ID
     *
     * @param username the username of the user
     * @param passwordHash the hashed password of a user
     */
    public User(String username, String passwordHash) {
        this(-1, username, passwordHash);
    }

    /**
     *Gets the user's ID.
     *
     * @return the ID of the user
     */
    public int getId() { return id; }

    /**
     * Sets the user's ID
     *
     * @param id the new ID for the user
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the user's username.
     *
     * @return the users username
     */
    public String getUsername() { return username; }

    /**
     * Sets the user's username.
     *
     * @param username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets the user's hashed password.
     *
     * @return the hashed password for the user
     */
    public String getPasswordHash() { return passwordHash; }

    /**
     * Sets the user's hashed password.
     *
     * @param passwordHash the new hashed password for the user
     */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}