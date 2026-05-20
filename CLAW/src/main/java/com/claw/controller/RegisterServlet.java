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
@WebServlet(&quot;/register&quot;)
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    /**
     * Show the registration form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
// Forward to the JSP form
        req.getRequestDispatcher(&quot;/register.jsp&quot;).forward(req, resp);
    }
    /**
     * Process registration submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
// Capture user input from the form
        String username = req.getParameter(&quot;username&quot;);
        String email = req.getParameter(&quot;email&quot;);
        String password = req.getParameter(&quot;password&quot;);
// --- STEP 1: VALIDATE UNIQUE EMAIL ---
        if (userDAO.findByEmail(email) != null) {
// Email found in database: return error to the user
            req.setAttribute(&quot;error&quot;, &quot;Email is already registered.&quot;);
            req.getRequestDispatcher(&quot;/register.jsp&quot;).forward(req,
                    resp);
            return;
        }
// --- STEP 2: CREATE AND SAVE NEW USER ---
// 1. Hash the password before storage for security (so it&#39;s
        not plain text)
        String hashedPassword = PasswordUtils.hashPassword(password);

// 2. Build the User object with the hashed password
        User newUser = new User(username, email, hashedPassword);
// 3. Save to database (will use SQL if available, otherwise
        Mock)
        userDAO.save(newUser);
// Audit Log successful registration
        AuditLogDAO.logEvent(username, &quot;Registration&quot;,
&quot;New user registered: &quot; + email + &quot; (Username: &quot; +
                username + &quot;)&quot;);
// --- STEP 3: SUCCESS REDIRECT ---
// Send user to login page with a success message flag
        resp.sendRedirect(req.getContextPath() +
                &quot;/login?registered=true&quot;);
    }
}