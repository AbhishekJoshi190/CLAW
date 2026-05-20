package com.claw.controller;

import com.claw.dao.UserDAO;
import com.claw.model.User;

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
 * Servlet handling user profile management.
 * Allows users to update their username and profile picture.
 */
@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB: Cache small files in memory
        maxFileSize = 1024 * 1024 * 10, // 10MB: Max file size
        maxRequestSize = 1024 * 1024 * 50 // 50MB: Max total request size
)
public class ProfileServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    /**
     * Display the profile page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // Redirect to login if user is not authenticated
        if (session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }

    /**
     * Handle profile updates (username and picture).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Security check
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // --- UPDATE 1: USERNAME ---
        String newUsername = req.getParameter("username");
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            user.setUsername(newUsername);
        }

        // --- UPDATE 2: PROFILE PICTURE ---
        Part filePart = req.getPart("profilePicture");
        if (filePart != null && filePart.getSize() > 0) {
            // Generate a unique filename
            String fileName = UUID.randomUUID().toString() + "_" + getSubmittedFileName(filePart);

            // Define target path for profile images
            String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator
                    + "profiles";

            // Create directory if missing
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file to the server
            filePart.write(uploadPath + File.separator + fileName);
            // Update the user object with the new URL
            user.setProfilePictureUrl(req.getContextPath() + "/images/profiles/" + fileName);
        }

        // Save changes to the SQL Database
        userDAO.update(user);

        // Update the session to reflect changes immediately
        session.setAttribute("user", user);
        req.setAttribute("success", "Profile updated successfully!");
        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }

    /**
     * Extracts filename from Content-Disposition header.
     */
    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                // Browser-specific path cleaning
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return "uploaded_file.jpg";
    }
}
