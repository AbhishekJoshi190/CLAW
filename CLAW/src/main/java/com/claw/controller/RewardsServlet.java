package com.claw.controller;

import com.claw.dao.UserDAO;
import com.claw.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet handling the reward redemption system.
 * Allows users to spend their earned points on exclusive perks.
 */
@WebServlet("/rewards")
public class RewardsServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    /**
     * Display the rewards catalog.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/rewards.jsp").forward(req, resp);
    }

    /**
     * Process a reward redemption request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Safety check: Only logged-in users can redeem rewards
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Capture which reward was selected and how much it costs
        String rewardName = req.getParameter("rewardName");
        int cost = Integer.parseInt(req.getParameter("cost"));

        // --- STEP 1: VERIFY POINTS BALANCE ---
        if (user.getPoints() >= cost) {
            // --- STEP 2: DEDUCT POINTS ---
            user.setPoints(user.getPoints() - cost);

            // Persist the new point balance to the SQL Database
            userDAO.update(user);

            // Update session so the UI shows the new balance
            session.setAttribute("user", user);

            req.setAttribute("success", "Successfully redeemed: " + rewardName + "! We will email you the details.");
        } else {
            // --- STEP 3: HANDLE INSUFFICIENT POINTS ---
            req.setAttribute("error", "Not enough points to redeem " + rewardName + ".");
        }

        // Return to the rewards page to show the result
        req.getRequestDispatcher("/rewards.jsp").forward(req, resp);
    }
}
