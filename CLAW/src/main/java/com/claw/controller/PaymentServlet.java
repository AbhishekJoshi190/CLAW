package com.claw.controller;

import com.claw.dao.AuditLogDAO;
import com.claw.dao.OrderDAO;
import com.claw.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet handling the payment simulation flow.
 * Interacts with the 'pendingOrder' stored in the session.
 */
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();

    /**
     * Show the payment gateway simulation page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Order pendingOrder = (Order) session.getAttribute("pendingOrder");

        // Safety check: Don't show payment page if there is no order to pay for
        if (pendingOrder == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Pass the selected method (esewa/khalti/card) to the JSP
        req.setAttribute("method", req.getParameter("method"));
        req.getRequestDispatcher("/payment.jsp").forward(req, resp);
    }

    /**
     * Process the "Success" response from the payment simulator.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Order pendingOrder = (Order) session.getAttribute("pendingOrder");

        if (pendingOrder != null) {
            // --- STEP 1: MARK ORDER AS PAID ---
            pendingOrder.setStatus("PROCESSING");

            // --- STEP 2: PERSIST TO DATABASE ---
            orderDAO.save(pendingOrder);

            // Audit Log successful online payment placement
            AuditLogDAO.logEvent(pendingOrder.getUser().getUsername(), "Order Placed",
                    "Order #" + pendingOrder.getId() + " paid and placed via " + pendingOrder.getPaymentMethod()
                            + " for NPR " + pendingOrder.getTotalAmount() + ".");

            // --- STEP 3: CLEAN UP SESSION ---
            session.removeAttribute("pendingOrder");
            session.removeAttribute("cart"); // The purchase is complete, so empty the cart

            // --- STEP 4: SUCCESS REDIRECT ---
            req.setAttribute("success", "Payment Successful! Order placed. Reference: " + pendingOrder.getId());
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } else {
            // If something went wrong, send back to cart
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
