package com.claw.dao;

import com.claw.model.User;
import com.claw.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User-related database operations.
 */
public class UserDAO {

    /**
     * Finds a user by their email address.
     * This is the primary method used for Login and checking if an email exists.
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        // Step 1: Try to connect to the real MySQL database
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Step 2: If found in SQL, build the User object and return it
            if (rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
                user.setProfilePictureUrl(rs.getString("profile_picture_url"));
                user.setPoints(rs.getInt("points"));
                user.setAdmin(rs.getBoolean("is_admin"));
                return user;
            }
        } catch (SQLException e) {
            // Step 3: FALLBACK - If MySQL fails (e.g., not installed), use the MockDatabase
            // instead.
            // This ensures the app works on computers without MySQL.
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findByEmail().");
            return com.claw.model.MockDatabase.getUserByEmail(email);
        }
        return null;
    }

    /**
     * Saves a new user to the database (Registration).
     */
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, email, password, profile_picture_url, points, is_admin) VALUES (?, ?, ?, ?, ?, ?)";
        // Step 1: Attempt to save to MySQL
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getProfilePictureUrl());
            stmt.setInt(5, user.getPoints());
            stmt.setBoolean(6, user.isAdmin());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Step 2: FALLBACK - If MySQL is down, save the user into the temporary
            // MockDatabase (RAM)
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for save().");
            com.claw.model.MockDatabase.addUser(user);
            return true; // Return true so the Servlet thinks it succeeded
        }
    }

    /**
     * Updates an existing user's information.
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, profile_picture_url = ?, points = ?, is_admin = ? WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getProfilePictureUrl());
            stmt.setInt(3, user.getPoints());
            stmt.setBoolean(4, user.isAdmin());
            stmt.setString(5, user.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all users from the database.
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
                user.setProfilePictureUrl(rs.getString("profile_picture_url"));
                user.setPoints(rs.getInt("points"));
                user.setAdmin(rs.getBoolean("is_admin"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findAll().");
            return com.claw.model.MockDatabase.getAllUsers();
        }
        return users;
    }
}
