package com.claw.controller;

import com.claw.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet handling user logout.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Terminate the user session and redirect to homepage.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Securely terminate the session
        SessionUtils.logout(req);

        // Return to homepage
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
