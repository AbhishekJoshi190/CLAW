package com.claw.model;

public class Product {
    // Unique identifier for the product (e.g. "s1", "h2")
    private String id;

    // Display name of the streetwear item
    private String name;

    // Price of the item in NPR
    private double price;

    // Group name (e.g. "shirts", "hoodies", "pants")
    private String category;

    // Path to the product image on the server
    private String imageUrl;

    /**
     * Default constructor for frameworks.
     */
    public Product() {
    }

    /**
     * Full constructor to initialize a new product catalog item.
     */
    public Product(String id, String name, double price, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // --- Getters & Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
