package com.claw.controller;

import com.claw.dao.ProductDAO;
import com.claw.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for displaying the details of a single product.
 * Used for the product customization and add-to-cart page.
 */
@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the unique ID of the product from the URL (e.g., /product?id=s1)
        String id = request.getParameter("id");

        // Find the product details in our SQL database
        Product product = productDAO.findById(id);

        // If the product doesn't exist, send the user back to the shop
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/shop");
            return;
        }

        // Pass the product object to the detail page (product.jsp)
        request.setAttribute("product", product);
        request.getRequestDispatcher("/product.jsp").forward(request, response);
    }
}
