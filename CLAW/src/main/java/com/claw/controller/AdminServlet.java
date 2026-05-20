package com.claw.controller;

import com.claw.dao.AuditLogDAO;
import com.claw.dao.OrderDAO;
import com.claw.dao.UserDAO;
import com.claw.model.Order;
import com.claw.model.User;
import com.claw.util.ReportGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

/**
 * Servlet for the Admin "Command Center" & Report Hub.
 * Handles order fulfillment, role toggling, audit logging, and report
 * exporting.
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    /**
     * Display the Admin Dashboard or handle CSV Exports.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        // --- SECURITY CHECK: ONLY ADMINS ALLOWED ---
        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // --- EXPORT HANDLE INCOMING REQUESTS ---
        String exportType = req.getParameter("export");
        if (exportType != null) {
            handleExport(exportType, req, resp);
            return;
        }

        // Pass lists, audit logs, and reports to JSP
        List<Order> allOrders = orderDAO.findAll();
        req.setAttribute("orders", allOrders);
        req.setAttribute("usersList", userDAO.findAll());
        req.setAttribute("auditLogs", auditLogDAO.findAll());
        req.setAttribute("report", ReportGenerator.generateReport(allOrders));

        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    /**
     * Handle admin actions (confirm delivery, toggle user roles).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Re-validate security for POST requests
        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String action = req.getParameter("action");

        // --- ACTION 1: CONFIRM DELIVERY ---
        if ("confirmDelivery".equals(action)) {
            String orderId = req.getParameter("orderId");
            Order order = orderDAO.findById(orderId);

            // Update order status and credit the user with reward points
            if (order != null && !"DELIVERED".equals(order.getStatus())) {
                order.setStatus("DELIVERED");
                orderDAO.updateStatus(orderId, "DELIVERED");

                User orderUser = userDAO.findByEmail(order.getUser().getEmail());
                int pointsAwarded = 0;
                if (orderUser != null) {
                    pointsAwarded = order.getPotentialPoints();
                    orderUser.setPoints(orderUser.getPoints() + pointsAwarded);
                    userDAO.update(orderUser);
                }

                // Audit Log successful delivery confirmation
                AuditLogDAO.logEvent(currentUser.getUsername(), "Order Delivered",
                        "Marked Order #" + orderId + " as DELIVERED. Awarded " + pointsAwarded + " points to "
                                + order.getUser().getEmail());

                req.setAttribute("success", "Order #" + orderId + " marked as delivered! Points awarded.");
            }
        }
        // --- ACTION 2: TOGGLE ADMIN ROLE ---
        else if ("toggleAdmin".equals(action)) {
            String userEmail = req.getParameter("userEmail");
            User userToToggle = userDAO.findByEmail(userEmail);

            if (userToToggle != null) {
                // Safeguard: Prevent admin from demoting themselves
                if (userToToggle.getEmail().equals(currentUser.getEmail())) {
                    req.setAttribute("error", "You cannot revoke your own admin status!");
                } else {
                    // Flip the boolean flag
                    userToToggle.setAdmin(!userToToggle.isAdmin());
                    userDAO.update(userToToggle);

                    String status = userToToggle.isAdmin() ? "promoted to Admin" : "demoted to User";

                    // Audit Log role change
                    AuditLogDAO.logEvent(currentUser.getUsername(), "Role Change",
                            (userToToggle.isAdmin() ? "Promoted " : "Revoked admin role from ")
                                    + userToToggle.getUsername() + " (" + userToToggle.getEmail() + ")");

                    req.setAttribute("success", "User " + userToToggle.getUsername() + " has been " + status + ".");
                }
            }
        }

        // Refresh all data for the dashboard
        List<Order> allOrders = orderDAO.findAll();
        req.setAttribute("orders", allOrders);
        req.setAttribute("usersList", userDAO.findAll());
        req.setAttribute("auditLogs", auditLogDAO.findAll());
        req.setAttribute("report", ReportGenerator.generateReport(allOrders));

        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    /**
     * Exports metrics as a clean downloadable CSV file.
     */
    private void handleExport(String type, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/csv");

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");
        String adminName = currentUser != null ? currentUser.getUsername() : "Admin";

        if ("sales".equals(type)) {
            resp.setHeader("Content-Disposition", "attachment; filename=\"CLAW_Sales_Report.csv\"");
            PrintWriter writer = resp.getWriter();
            writer.println("Order ID,Customer,Email,Address,City,Total (NPR),Payment Method,Status,Items Summary");

            List<Order> orders = orderDAO.findAll();
            for (Order o : orders) {
                String address = o.getStreetAddress().replace("\"", "\"\"");
                String summary = o.getItemsSummary().replace("\"", "\"\"");
                writer.printf("\"#%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\"\n",
                        o.getId(), o.getUser().getUsername(), o.getEmail(), address, o.getCity(),
                        (double) o.getTotalAmount(), o.getPaymentMethod(), o.getStatus(), summary);
            }
            writer.flush();
            AuditLogDAO.logEvent(adminName, "Export Data", "Exported sales transactions report as CSV.");
        } else if ("logs".equals(type)) {
            resp.setHeader("Content-Disposition", "attachment; filename=\"CLAW_System_Audit_Logs.csv\"");
            PrintWriter writer = resp.getWriter();
            writer.println("Log ID,Username,Action,Details,Timestamp");

            List<com.claw.model.AuditLog> logs = auditLogDAO.findAll();
            for (com.claw.model.AuditLog l : logs) {
                String details = l.getDetails().replace("\"", "\"\"");
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        l.getId(), l.getUsername(), l.getAction(), details, l.getCreatedAt().toString());
            }
            writer.flush();
            AuditLogDAO.logEvent(adminName, "Export Data", "Exported system audit logs history as CSV.");
        } else if ("pl".equals(type)) {
            resp.setHeader("Content-Disposition", "attachment; filename=\"CLAW_Profit_Loss_Statement.csv\"");
            PrintWriter writer = resp.getWriter();

            List<Order> orders = orderDAO.findAll();
            ReportGenerator.ReportData report = ReportGenerator.generateReport(orders);

            writer.println("CLAW STREETWEAR - PROFIT & LOSS STATEMENT");
            writer.println("Generated At," + new Timestamp(System.currentTimeMillis()).toString());
            writer.println("Exported By," + adminName);
            writer.println();
            writer.println("Line Item,Amount (NPR),Description");
            writer.printf("Gross Revenue (All Orders),%.2f,\"Accumulated retail sales of all placed transactions\"\n",
                    report.grossSales);
            writer.printf("Realized Revenue (Delivered Orders),%.2f,\"Sourced funds from completed orders\"\n",
                    report.realizedRevenue);
            writer.printf(
                    "Cost of Goods Sold (COGS),%.2f,\"Estimated product manufacturing and supply cost (50%% of Sales)\"\n",
                    report.cogs);
            writer.println("----------------------------------------");
            writer.printf("Gross Profit,%.2f,\"Realized Revenue - COGS\"\n", report.grossProfit);
            writer.println("----------------------------------------");
            writer.println("Operating Expenses,,");
            writer.printf("Hosting & Domain,%.2f,\"Fixed application hosting & web infrastructure fees\"\n",
                    report.expenseHosting);
            writer.printf(
                    "Logistics & Delivery,%.2f,\"Courier distribution, packing & shipping fees (8%% of Sales)\"\n",
                    report.expenseLogistics);
            writer.printf(
                    "Packaging & Branding,%.2f,\"Premium packaging boxes, tags, and labels (NPR 150 per order)\"\n",
                    report.expensePackaging);
            writer.printf(
                    "Marketing & Promos,%.2f,\"Ad acquisition campaigns, rewards credit & marketing outreach (12%% of Sales)\"\n",
                    report.expenseMarketing);
            writer.printf("Total Expenses,%.2f,\"Aggregated hosting, logistics, packaging, and advertising costs\"\n",
                    report.totalExpenses);
            writer.println("----------------------------------------");
            writer.printf("Net Profit,%.2f,\"Gross Profit - Total Expenses\"\n", report.netProfit);
            writer.printf("Net Profit Margin,%.2f%%,\"Net Income percentage on Sourced Funds\"\n",
                    report.netProfitMargin);
            writer.flush();
            AuditLogDAO.logEvent(adminName, "Export Data", "Exported Profit & Loss statement as CSV.");
        }
    }
}
