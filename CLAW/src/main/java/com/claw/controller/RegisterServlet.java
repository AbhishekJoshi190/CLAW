package com.claw.controller;

import com.claw.dao.AuditLogDAO;
import com.claw.dao.UserDAO;
import com.claw.model.User;
import com.claw.util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet handling user registration.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    /**
     * Show the registration form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to the JSP form
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    /**
     * Process registration submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Capture user input from the form
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // --- STEP 1: VALIDATE UNIQUE EMAIL ---
        if (userDAO.findByEmail(email) != null) {
            // Email found in database: return error to the user
            req.setAttribute("error", "Email is already registered.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // --- STEP 2: CREATE AND SAVE NEW USER ---
        // 1. Hash the password before storage for security (so it's not plain text)
        String hashedPassword = PasswordUtils.hashPassword(password);

        // 2. Build the User object with the hashed password
        User newUser = new User(username, email, hashedPassword);

        // 3. Save to database (will use SQL if available, otherwise Mock)
        userDAO.save(newUser);

        // Audit Log successful registration
        AuditLogDAO.logEvent(username, "Registration",
                "New user registered: " + email + " (Username: " + username + ")");

        // --- STEP 3: SUCCESS REDIRECT ---
        // Send user to login page with a success message flag
        resp.sendRedirect(req.getContextPath() + "/login?registered=true");
    }
}
