package com.claw.util;
import com.claw.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * Utility for managing user sessions consistently across the
 application.
 */
public class SessionUtils {
    private static final String USER_KEY = &quot;user&quot;;
    /**
     * Stores the user object in the session.
     * This &quot;remembers&quot; the user as they move between different pages.
     */
    public static void login(HttpServletRequest request, User user) {
// Step 1: Create a session (or get the existing one)
        HttpSession session = request.getSession(true);
// Step 2: Save the user data into the session memory
        session.setAttribute(USER_KEY, user);
    }
    /**
     * Retrieves the current logged-in user.
     * Returns null if no one is logged in.
     */
    public static User getUser(HttpServletRequest request) {
// Get the session only if it already exists (don&#39;t create a
        new empty one)
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(USER_KEY);
        }
        return null;
    }
    /**
     * Checks if a user is currently logged in.
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getUser(request) != null;
    }

    /**
     * Checks if the logged-in user is an administrator.
     */
    public static boolean isAdmin(HttpServletRequest request) {
        User user = getUser(request);
        return user != null &amp;&amp; user.isAdmin();
    }
    /**
     * Clears the user session (Logout).
     * This effectively &quot;forgets&quot; the user.
     */
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
// Step 1: Remove the user attribute
            session.removeAttribute(USER_KEY);
// Step 2: Destroy the session entirely for security
            session.invalidate();
        }
    }
}