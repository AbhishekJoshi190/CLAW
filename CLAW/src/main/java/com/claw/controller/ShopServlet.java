package com.claw.controller;

import com.claw.dao.ProductDAO;
import com.claw.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet handling the Shop gallery view.
 * Supports filtering products by category and keyword search.
 */
@WebServlet("/shop")
public class ShopServlet extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Capture category and search parameters from the URL query string
        String category = request.getParameter("category");
        String search = request.getParameter("search");

        // Clean up and capitalize search query to maintain streetwear design guidelines
        String capitalizedSearch = capitalize(search);

        // Fetch products matching the selected category and capitalized search term
        List<Product> products = productDAO.findFiltered(category, capitalizedSearch);

        // Pass standard attributes to the JSP for rendering
        request.setAttribute("products", products);
        request.setAttribute("currentCategory", category == null ? "all" : category);
        request.setAttribute("searchQuery", capitalizedSearch);

        // Forward to the shop layout
        request.getRequestDispatcher("/shop.jsp").forward(request, response);
    }

    /**
     * Helper to automatically capitalize the first letter of each word in the search string.
     * This keeps product queries consistently styled with CLAW's streetwear titles.
     */
    private String capitalize(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        String[] words = str.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.length() > 0) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                  .append(w.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
