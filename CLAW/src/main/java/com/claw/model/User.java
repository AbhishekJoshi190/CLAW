package com.claw.model;

public class User {
    // The display name or nickname of the user
    private String username;

    // The user's primary contact and login email
    private String email;

    // Password for authentication
    private String password;

    // URL to the user's avatar image
    private String profilePictureUrl;

    // Loyalty points accumulated from orders
    private int points;

    // Whether the user has administrative access to the Command Center
    private boolean isAdmin;

    /**
     * Constructor for creating a new account (Registration).
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        // Assign a default portrait as profile picture
        this.profilePictureUrl = "https://source.unsplash.com/150x150/?portrait,streetwear";
        this.points = 0;
        this.isAdmin = false;
    }

    // --- GETTERS & SETTERS ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Check if user is an administrator.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Promote or demote a user's admin status.
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
