package com.claw.controller;
import com.claw.dao.AuditLogDAO;
import com.claw.dao.UserDAO;
import com.claw.model.User;
import com.claw.util.PasswordUtils;
import com.claw.util.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Servlet handling user authentication (Login).
 */
@WebServlet(&quot;/login&quot;)
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    /**
     * Show the login form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
// Forward to the JSP form
        req.getRequestDispatcher(&quot;/login.jsp&quot;).forward(req, resp);
    }

    /**
     * Process login submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws ServletException, IOException {
// Capture credentials from the form
        String email = req.getParameter(&quot;email&quot;);
        String password = req.getParameter(&quot;password&quot;);
// --- STEP 1: FIND USER BY EMAIL ---
        User user = userDAO.findByEmail(email);
// --- STEP 2: VERIFY HASHED PASSWORD ---
// We use checkPassword because we cannot compare hashes directly
        using
// .equals()
        if (user != null &amp;&amp; PasswordUtils.checkPassword(password,
                user.getPassword())) {
// Success: Securely create a session to &quot;log in&quot; the user
            SessionUtils.login(req, user);
// Audit Log successful login
            AuditLogDAO.logEvent(user.getUsername(), &quot;Login&quot;, &quot;User logged
            in successfully: &quot; + user.getEmail());
// Redirect to homepage
            resp.sendRedirect(req.getContextPath() + &quot;/&quot;);
        } else {
// Failure: Return to login page with an error message
            req.setAttribute(&quot;error&quot;, &quot;Invalid email or password.&quot;);
            req.getRequestDispatcher(&quot;/login.jsp&quot;).forward(req, resp);
        }
    }
}