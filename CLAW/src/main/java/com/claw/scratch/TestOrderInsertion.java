package com.claw.scratch;

import com.claw.dao.OrderDAO;
import com.claw.dao.ProductDAO;
import com.claw.model.CartItem;
import com.claw.model.Order;
import com.claw.model.Product;
import com.claw.model.User;
import com.claw.model.MockDatabase;
import com.claw.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestOrderInsertion {
    public static void main(String[] args) {
        System.out.println("CLAW E-Commerce Test: Initiating mock order insertion...");

        // Ensure database has products to satisfy foreign keys
        ProductDAO productDAO = new ProductDAO();
        try {
            if (productDAO.findAll().isEmpty()) {
                System.out.println("CLAW E-Commerce Test: Products table is empty. Seeding catalog items...");
                productDAO.insertAll(MockDatabase.getAllProducts());
                System.out.println("CLAW E-Commerce Test: Seeding complete!");
            }
        } catch (Exception e) {
            System.err.println("CLAW E-Commerce Test: Failed to seed products table!");
            e.printStackTrace();
            return;
        }

        // 1. Create a dummy User
        User testUser = new User("Admin", "admin@claw.com", "admin123");

        // 2. Create products to purchase (using existing real IDs in the seeded catalog)
        Product shirt = new Product("s1", "Essential Black Shirt", 3000.0, "shirts", "images/shirt-black.jpg");
        Product hoodie = new Product("h1", "Essential Black Hoodie", 5500.0, "hoodies", "images/hoodie-black.jpg");

        // 3. Package as Cart items
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(shirt, 2, "M", "", null, null)); // 2x Shirt
        cartItems.add(new CartItem(hoodie, 1, "L", "", null, null)); // 1x Hoodie

        // 4. Instantiate Order
        Order order = new Order(testUser, "123 Street", "Kathmandu", "44600", "987654321", "admin@claw.com",
                "2x Essential Black Shirt (M), 1x Essential Black Hoodie (L)", 11500, 115, "COD");
        order.setItems(cartItems);

        // 5. Save order using modified OrderDAO
        OrderDAO orderDAO = new OrderDAO();
        boolean saveResult = orderDAO.save(order);
        System.out.println("CLAW E-Commerce Test: OrderDAO.save() result: " + (saveResult ? "SUCCESS" : "FAILED"));

        // 6. Verify table contents in MySQL
        System.out.println("\n--- Querying 'order_products' Table inside MySQL ---");
        String sql = "SELECT * FROM order_products WHERE order_id = '" + order.getId() + "'";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rowsFound = 0;
            while (rs.next()) {
                rowsFound++;
                System.out.printf("Row %d -> ID: %d | OrderID: %s | ProductID: %s | Quantity: %d\n",
                        rowsFound,
                        rs.getInt("id"),
                        rs.getString("order_id"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"));
            }

            if (rowsFound == 0) {
                System.out.println("Result: Empty! No items were saved to 'order_products'.");
            } else {
                System.out.println("Result: SUCCESS! " + rowsFound + " relational items verified inside order_products!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
