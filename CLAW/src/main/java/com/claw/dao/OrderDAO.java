package com.claw.dao;

import com.claw.model.Order;
import com.claw.model.User;
import com.claw.model.CartItem;
import com.claw.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order-related database operations.
 */
public class OrderDAO {

    /**
     * Saves a new order to the database.
     */
    public boolean save(Order order) {
        String sql = "INSERT INTO orders (id, user_id, total_amount, payment_method, street_address, city, postal_code, phone, email, status, items_summary) "
                +
                "VALUES (?, (SELECT id FROM users WHERE email = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getId());
            stmt.setString(2, order.getUser().getEmail()); // Link via email to get ID
            stmt.setInt(3, order.getTotalAmount());
            stmt.setString(4, order.getPaymentMethod());
            stmt.setString(5, order.getStreetAddress());
            stmt.setString(6, order.getCity());
            stmt.setString(7, order.getPostalCode());
            stmt.setString(8, order.getPhone());
            stmt.setString(9, order.getEmail());
            stmt.setString(10, order.getStatus());
            stmt.setString(11, order.getItemsSummary());

            boolean orderSaved = stmt.executeUpdate() > 0;
            if (orderSaved && order.getItems() != null && !order.getItems().isEmpty()) {
                String itemSql = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                    for (CartItem item : order.getItems()) {
                        itemStmt.setString(1, order.getId());
                        itemStmt.setString(2, item.getProduct().getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }
            }
            return orderSaved;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for save().");
            com.claw.model.MockDatabase.addOrder(order);
            return true;
        }
    }

    /**
     * Retrieves all orders from the database, including the associated user
     * details.
     */
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as u_name, u.email as u_email FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Reconstruct User object for the order
                User user = new User(rs.getString("u_name"), rs.getString("u_email"), "");

                Order order = new Order();
                order.setId(rs.getString("id"));
                order.setUser(user);
                order.setTotalAmount(rs.getInt("total_amount"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setStreetAddress(rs.getString("street_address"));
                order.setCity(rs.getString("city"));
                order.setPostalCode(rs.getString("postal_code"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setStatus(rs.getString("status"));
                order.setItemsSummary(rs.getString("items_summary"));

                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findAll().");
            return com.claw.model.MockDatabase.getOrders();
        }
        return orders;
    }

    /**
     * Updates the status of an order (e.g., to DELIVERED).
     */
    public boolean updateStatus(String id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Finds an order by its unique ID.
     */
    public Order findById(String id) {
        String sql = "SELECT o.*, u.username as u_name, u.email as u_email FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("u_name"), rs.getString("u_email"), "");
                Order order = new Order();
                order.setId(rs.getString("id"));
                order.setUser(user);
                order.setTotalAmount(rs.getInt("total_amount"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setStreetAddress(rs.getString("street_address"));
                order.setCity(rs.getString("city"));
                order.setPostalCode(rs.getString("postal_code"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setStatus(rs.getString("status"));
                order.setItemsSummary(rs.getString("items_summary"));
                return order;
            }
        } catch (SQLException e) {
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findById().");
            return com.claw.model.MockDatabase.getOrderById(id);
        }
        return null;
    }
}
