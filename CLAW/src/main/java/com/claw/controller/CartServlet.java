package com.claw.controller;

import com.claw.model.Cart;
import com.claw.model.CartItem;
import com.claw.dao.ProductDAO;
import com.claw.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Servlet responsible for managing the shopping cart.
 * Handles adding new items (with customization) and removing existing items.
 */
@WebServlet("/cart")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB: Cache small files in memory
        maxFileSize = 1024 * 1024 * 10, // 10MB: Maximum single file size
        maxRequestSize = 1024 * 1024 * 50 // 50MB: Maximum total request size
)
public class CartServlet extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Display the cart page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward the request to the cart.jsp page for rendering
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    /**
     * Handle cart updates (Add to cart / Remove from cart).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the action and cart from the session
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        // --- SECTION: REMOVE ITEM ---
        if ("remove".equals(action)) {
            if (cart != null) {
                try {
                    // Remove item by its index in the list
                    int index = Integer.parseInt(request.getParameter("index"));
                    cart.removeByIndex(index);
                } catch (NumberFormatException e) {
                    // Fail gracefully if index is not a number
                }
            }
        }
        // --- SECTION: ADD ITEM ---
        else {
            String productId = request.getParameter("productId");
            Product product = productDAO.findById(productId);

            if (product != null) {
                // Get basic attributes
                String size = request.getParameter("size");
                String hipsSize = request.getParameter("hipsSize");

                // Handle custom design uploads (saves to /images/custom/)
                String frontUrl = saveUploadedFile(request.getPart("frontDesign"), request);
                String backUrl = saveUploadedFile(request.getPart("backDesign"), request);

                // Initialize cart if it doesn't exist
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }

                // Create new cart item and add it to the cart
                CartItem newItem = new CartItem(product, 1, size, hipsSize, frontUrl, backUrl);
                cart.addCustomItem(newItem);
            }
        }

        // Redirect back to the cart page to see the updated contents
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    /**
     * Utility method to save uploaded design files to the server's filesystem.
     */
    private String saveUploadedFile(Part filePart, HttpServletRequest request) throws IOException {
        // Check if a file was actually uploaded
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }

        // Extract the original filename
        String originalFileName = getSubmittedFileName(filePart);
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return null;
        }

        // Create a unique filename to prevent overwriting
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // Define the target directory on the server
        String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "custom";

        // Create the directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Write the file to the disk
        filePart.write(uploadPath + File.separator + fileName);

        // Return the relative URL path to be stored in the database/session
        return request.getContextPath() + "/images/custom/" + fileName;
    }

    /**
     * Extracts the filename from the HTTP Content-Disposition header.
     */
    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                // Handle different browser filename paths (IE fix)
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}
