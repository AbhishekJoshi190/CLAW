package com.claw.util;

import com.claw.dao.ProductDAO;
import com.claw.model.MockDatabase;
import com.claw.model.Product;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * Automates the initial setup of the SQL database.
 * Migrates hardcoded products from MockDatabase to the MySQL tables on startup.
 */
@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("CLAW System: Starting background SQL Database initialization...");

        // Run migration in a separate thread to avoid blocking Tomcat startup
        new Thread(() -> {
            try {
                ProductDAO productDAO = new ProductDAO();

                // --- STEP 1: CHECK IF DB IS EMPTY ---
                List<Product> existingProducts = productDAO.findAll();

                if (existingProducts.isEmpty()) {
                    System.out.println("CLAW System: No products found in SQL. Migrating from Mock data...");

                    // --- STEP 2: FETCH MOCK DATA ---
                    List<Product> mockProducts = MockDatabase.getAllProducts();

                    // --- STEP 3: BULK INSERT ---
                    productDAO.insertAll(mockProducts);

                    System.out
                            .println("CLAW System: Migration complete! " + mockProducts.size() + " products imported.");
                } else {
                    System.out.println("CLAW System: SQL Database already contains product data. Skipping migration.");
                }
            } catch (Exception e) {
                System.err.println("CLAW System ERROR: Background initialization failed!");
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup if necessary
    }
}
