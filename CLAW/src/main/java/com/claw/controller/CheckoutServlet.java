package com.claw.controller;

import com.claw.dao.AuditLogDAO;
import com.claw.dao.OrderDAO;
import com.claw.model.Cart;
import com.claw.model.CartItem;
import com.claw.model.Order;
import com.claw.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet handling the final stage of the purchase (Checkout).
 * Calculates reward points and routes to payment or order completion.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();

    /**
     * Display the checkout form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Security check: User must be logged in to checkout
        if (session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Safety check: Cannot checkout with an empty cart
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
    }

    /**
     * Process the order submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Capture shipping and payment details
        String paymentMethod = req.getParameter("paymentMethod");
        String streetAddress = req.getParameter("streetAddress");
        String city = req.getParameter("city");
        String postalCode = req.getParameter("postalCode");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");

        // --- STEP 1: CALCULATE POTENTIAL REWARD POINTS ---
        // Points are calculated based on item quantity and total spend
        int potentialPoints = 0;
        StringBuilder summary = new StringBuilder();

        for (CartItem item : cart.getItems()) {
            // Base points for buying an item
            potentialPoints += (5 * item.getQuantity());
            // Bonus points based on price (1 point per 100 NPR)
            potentialPoints += (int) (item.getTotalPrice() / 100);

            // Build a human-readable summary of the items for the Admin Panel
            summary.append(item.getQuantity()).append("x ").append(item.getProduct().getName());
            if (item.getSize() != null && !item.getSize().isEmpty()) {
                summary.append(" (Size: ").append(item.getSize()).append(")");
            }
            if (item.getHipsSize() != null && !item.getHipsSize().isEmpty()) {
                summary.append(" (Hips: ").append(item.getHipsSize()).append("\")");
            }
            if (item.getFrontDesignUrl() != null)
                summary.append(" [Custom Front]");
            if (item.getBackDesignUrl() != null)
                summary.append(" [Custom Back]");
            summary.append(", ");
        }

        // Clean up summary string
        if (summary.length() > 0) {
            summary.setLength(summary.length() - 2);
        }

        // --- STEP 2: CREATE THE ORDER OBJECT ---
        Order order = new Order(user, streetAddress, city, postalCode, phone, email,
                summary.toString(), (int) cart.getTotalPrice(), potentialPoints, paymentMethod);
        order.setItems(cart.getItems());

        // --- STEP 3: ROUTE BY PAYMENT METHOD ---
        if ("COD".equals(paymentMethod)) {
            // Cash on Delivery: Skip payment gateway and place order immediately
            order.setStatus("PROCESSING");
            orderDAO.save(order);

            // Audit Log successful COD order placement
            AuditLogDAO.logEvent(user.getUsername(), "Order Placed",
                    "Order #" + order.getId() + " placed via COD for NPR " + order.getTotalAmount() + ".");

            session.removeAttribute("cart"); // Empty cart

            req.setAttribute("success", "Order placed successfully! You will earn " + potentialPoints
                    + " points upon delivery. Reference: " + order.getId());
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } else {
            // Online Payment: Save order to session and redirect to the payment simulator
            session.setAttribute("pendingOrder", order);
            resp.sendRedirect(req.getContextPath() + "/payment?method=" + paymentMethod);
        }
    }
}
