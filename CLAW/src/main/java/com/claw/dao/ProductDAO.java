package com.claw.dao;

import com.claw.model.Product;
import com.claw.model.MockDatabase;
import com.claw.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product-related database operations.
 */
public class ProductDAO {

    /**
     * Retrieves all products from the database.
     */
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("image_url")));
            }
        } catch (SQLException e) {
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findAll().");
            return com.claw.model.MockDatabase.getAllProducts();
        }
        return products;
    }

    /**
     * Filters products by category.
     */
    public List<Product> findByCategory(String category) {
        if (category == null || category.isEmpty() || category.equalsIgnoreCase("all")) {
            return findAll();
        }

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("image_url")));
            }
        } catch (SQLException e) {
            System.err
                    .println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findByCategory().");
            return com.claw.model.MockDatabase.getProductsByCategory(category);
        }
        return products;
    }

    /**
     * Grabs a list of products filtered by category and search keyword.
     * We build the SQL query dynamically and safely use PreparedStatements
     * to shut down any sneaky SQL injection attempts. If our primary database
     * goes down, this gracefully drops back to our in-memory MockDatabase.
     */
    public List<Product> findFiltered(String category, String query) {
        List<Product> products = new ArrayList<>();

        // Start building the query. Using "WHERE 1=1" is a handy trick that lets
        // us append dynamic "AND" conditions without worrying about SQL syntax errors.
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Append category filter if the user chose a specific collection
        if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("all")) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        // Apply a case-insensitive search filter if they typed a search term.
        // Converting everything to lower case makes it work consistently across all collations.
        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE ?");
            params.add("%" + query.trim().toLowerCase() + "%");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Bind all parameters to their respective placeholders dynamically
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("image_url")));
            }
        } catch (SQLException e) {
            // If our SQL server is offline, we don't want to show the user an error page.
            // Let's log it internally and read from the mock database memory fallback instead!
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findFiltered().");
            return MockDatabase.findFiltered(category, query);
        }
        return products;
    }

    /**
     * Finds a product by its unique ID.
     */
    public Product findById(String id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("image_url"));
            }
        } catch (SQLException e) {
            System.err.println("CLAW System: SQL Connection failed. Falling back to MockDatabase for findById().");
            return MockDatabase.getProductById(id);
        }
        return null;
    }

    /**
     * Bulk inserts products (used for initialization).
     */
    public void insertAll(List<Product> products) {
        String sql = "INSERT IGNORE INTO products (id, name, price, category, image_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (Product p : products) {
                stmt.setString(1, p.getId());
                stmt.setString(2, p.getName());
                stmt.setDouble(3, p.getPrice());
                stmt.setString(4, p.getCategory());
                stmt.setString(5, p.getImageUrl());
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
