package com.claw.model;

import java.util.List;
import java.util.UUID;

// Force recompile
public class Order {
    // Unique identifier for the order (8-character alphanumeric string)
    private String id;

    // The user who placed the order
    private User user;

    // Detailed shipping and contact information
    private String streetAddress;
    private String city;
    private String postalCode;
    private String phone;
    private String email;

    // A text summary of all items in the order (e.g. "2x Hoodie (M)")
    private String itemsSummary;

    // Total price of the order in NPR
    private int totalAmount;

    // Reward points the user WILL earn once the order is delivered
    private int potentialPoints;

    // Method used for payment (esewa, khalti, card, or COD)
    private String paymentMethod;

    // Current fulfillment status of the order
    private String status; // Possible values: PENDING_PAYMENT, PROCESSING, DELIVERED

    // List of individual items in the order
    private List<CartItem> items;

    /**
     * Constructor to initialize a new Order with all required details.
     */
    public Order(User user, String streetAddress, String city, String postalCode, String phone, String email,
            String itemsSummary, int totalAmount, int potentialPoints, String paymentMethod) {

        // Generate a random unique ID for the order (e.g. "A1B2C3D4")
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Assign provided information to class fields
        this.user = user;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
        this.itemsSummary = itemsSummary;
        this.totalAmount = totalAmount;
        this.potentialPoints = potentialPoints;
        this.paymentMethod = paymentMethod;

        // Default status for a newly created order
        this.status = "PENDING_PAYMENT";
    }

    /**
     * Empty constructor for database loading.
     */
    public Order() {
    }

    // --- Getters and Setters for order properties ---

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setItemsSummary(String itemsSummary) {
        this.itemsSummary = itemsSummary;
    }

    public String getItemsSummary() {
        return itemsSummary;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setPotentialPoints(int potentialPoints) {
        this.potentialPoints = potentialPoints;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Updates the payment method (used if a user switches during checkout).
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    // Shipping info getters/setters
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Updates the status (e.g. when an admin marks it as DELIVERED).
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
