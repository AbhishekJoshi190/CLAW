package com.claw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory database simulation for the CLAW application.
 * Stores products, users, and orders until the server restarts.
 */
public class MockDatabase {
    // Shared list of all products in the shop
    private static final List<Product> products = new ArrayList<>();

    // Static block runs once when the class is loaded to pre-populate data
    static {
        // --- CATEGORY: SHIRTS ---
        products.add(new Product("s1", "Essential Black Shirt", 3000.00, "shirts", "images/shirt-black.jpg"));
        products.add(new Product("s2", "Plaid Flannel Shirt", 3500.00, "shirts", "images/shirt-flannel.jpg"));
        products.add(new Product("s3", "Crisp White Shirt", 3000.00, "shirts", "images/shirt-white.jpg"));
        products.add(new Product("s4", "Oversized Black Tee", 2000.00, "shirts", "images/tee-black.jpg"));
        products.add(new Product("s5", "Basic Grey Tee", 1800.00, "shirts", "images/tee-grey.jpg"));
        products.add(new Product("s6", "Olive Green Tee", 2000.00, "shirts", "images/tee-olive.jpg"));
        products.add(new Product("s7", "Classic White Tee", 1800.00, "shirts", "images/tee-white.jpg"));

        // --- CATEGORY: PANTS / BOTTOMS ---
        products.add(new Product("p1", "Beige Cargo Pants", 4500.00, "pants", "images/cargo-beige.jpg"));
        products.add(new Product("p2", "Tactical Black Cargo", 4800.00, "pants", "images/cargo-black.jpg"));
        products.add(new Product("p3", "Slim Black Jeans", 4000.00, "pants", "images/jeans-black.jpg"));
        products.add(new Product("p4", "Classic Blue Jeans", 4000.00, "pants", "images/jeans-blue.jpg"));
        products.add(new Product("p5", "Indigo Denim Jeans", 4200.00, "pants", "images/jeans-indigo.jpg"));
        products.add(new Product("p6", "Summer Black Shorts", 2500.00, "pants", "images/shorts-black.jpg"));

        // --- CATEGORY: HOODIES & SWEATS ---
        products.add(new Product("h1", "Essential Black Hoodie", 5500.00, "hoodies", "images/hoodie-black.jpg"));
        products.add(new Product("h2", "Earth Brown Hoodie", 5500.00, "hoodies", "images/hoodie-brown.jpg"));
        products.add(new Product("h3", "Cream Fleece Hoodie", 5800.00, "hoodies", "images/hoodie-cream.jpg"));
        products.add(new Product("h4", "Zip-Up Grey Hoodie", 6000.00, "hoodies", "images/hoodie-zip.jpg"));
        products.add(new Product("h5", "Grey Sweatshirt", 4500.00, "hoodies", "images/sweat-grey.jpg"));

        // --- CATEGORY: JACKETS / OUTERWEAR ---
        products.add(new Product("j1", "Lightweight Black Jacket", 7500.00, "jackets", "images/jacket-black.jpg"));
        products.add(new Product("j2", "Utility Cargo Jacket", 8000.00, "jackets", "images/jacket-cargo.jpg"));
        products.add(new Product("j3", "Classic Denim Jacket", 8500.00, "jackets", "images/jacket-denim.jpg"));
        products.add(new Product("j4", "Premium Leather Jacket", 15000.00, "jackets", "images/jacket-leather.jpg"));
        products.add(new Product("j5", "Winter Puffer Jacket", 12000.00, "jackets", "images/jacket-puffer.jpg"));

        // --- CATEGORY: ACCESSORIES ---
        products.add(new Product("a1", "Black Beanie", 1500.00, "accessories", "images/beanie-black.jpg"));
        products.add(new Product("a2", "Classic Black Cap", 1200.00, "accessories", "images/cap-black.jpg"));
    }

    /**
     * Retrieve all products in the database.
     */
    public static List<Product> getAllProducts() {
        return products;
    }

    /**
     * Filter products by their category name.
     */
    public static List<Product> getProductsByCategory(String category) {
        // Return all if no specific category is selected
        if (category == null || category.isEmpty() || category.equalsIgnoreCase("all")) {
            return products;
        }
        // Use Java Streams to find matches
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters products by category and searches case-insensitively by keyword.
     *
     * @param category The selected category (e.g. "shirts", "pants"). If null/empty or "all", all items are considered.
     * @param query The search term. If provided, filters items containing this keyword case-insensitively.
     * @return List of matching Products.
     */
    public static List<Product> findFiltered(String category, String query) {
        // Step 1: Pre-filter products by category using the existing helper method
        List<Product> list = getProductsByCategory(category);

        // Step 2: If search keyword is blank, return the category results directly
        if (query == null || query.trim().isEmpty()) {
            return list;
        }

        // Step 3: Perform case-insensitive search on the product names
        final String lowerQuery = query.toLowerCase().trim();
        return list.stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Find a single product using its unique ID.
     */
    public static Product getProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // --- USER MANAGEMENT ---
    private static final List<User> users = new ArrayList<>();

    static {
        // Create an initial Administrator account for testing
        User adminUser = new User("Admin", "admin@claw.com",
                "$2a$10$CwrNCO5ukNFjJvQ80GF3LOwgyOKEFW87daDJlBGoi2gEJgT9mjOIe");
        adminUser.setAdmin(true);
        users.add(adminUser);
    }

    /**
     * Find a user by their registered email address (used for Login).
     */
    public static User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of all registered users (used for Admin panel management).
     */
    public static List<User> getAllUsers() {
        return users;
    }

    /**
     * Save a new user to the database (Registration).
     */
    public static void addUser(User user) {
        users.add(user);
    }

    // --- ORDER MANAGEMENT ---
    private static final List<Order> orders = new ArrayList<>();

    /**
     * Retrieve all orders placed on the system.
     */
    public static List<Order> getOrders() {
        return orders;
    }

    /**
     * Save a new order.
     */
    public static void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Find an order by its generated 8-character ID.
     */
    public static Order getOrderById(String id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // --- SYSTEM AUDIT LOGGING ---
    private static final List<AuditLog> auditLogs = new java.util.concurrent.CopyOnWriteArrayList<>();

    static {
        // Pre-populate some historical mock logs for system demonstration
        long now = System.currentTimeMillis();
        auditLogs.add(new AuditLog(1, "System", "System Startup",
                "CLAW Streetwear application booted successfully in development mode.",
                new java.sql.Timestamp(now - 3600000L * 3))); // 3 hours ago
        auditLogs.add(new AuditLog(2, "admin@claw.com", "Role Promotion", "Promoted support@claw.com to Co-Admin role.",
                new java.sql.Timestamp(now - 3600000L * 2))); // 2 hours ago
        auditLogs.add(new AuditLog(3, "john@gmail.com", "Registration",
                "New user registered: john@gmail.com (Username: JohnDoe).", new java.sql.Timestamp(now - 5400000L))); // 1.5
        // hours
        // ago
        auditLogs.add(new AuditLog(4, "john@gmail.com", "Login", "User logged in: john@gmail.com.",
                new java.sql.Timestamp(now - 4320000L))); // 1.2 hours ago
        auditLogs.add(new AuditLog(5, "john@gmail.com", "Order Placed",
                "Order #7D4F28E1 placed via Card payment for NPR 8,500.00.", new java.sql.Timestamp(now - 1800000L))); // 30
        // mins
        // ago
        auditLogs.add(new AuditLog(6, "admin@claw.com", "Order Delivered",
                "Marked Order #7D4F28E1 as DELIVERED and awarded 85 reward points.",
                new java.sql.Timestamp(now - 600000L))); // 10 mins ago
    }

    /**
     * Retrieve all system audit logs.
     */
    public static List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    /**
     * Add a new audit log to the list.
     */
    public static void addAuditLog(AuditLog log) {
        // Assign a mock ID based on current list size
        log.setId(auditLogs.size() + 1);
        auditLogs.add(log);
    }
}
