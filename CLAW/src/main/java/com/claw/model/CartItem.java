package com.claw.model;

public class CartItem {
    // Basic product information
    private Product product;

    // Amount of this product in the cart
    private int quantity;

    // Customization attributes
    private String size; // e.g., "S", "M", "L"
    private String hipsSize; // For Bottoms (jeans, cargo)
    private String frontDesignUrl; // Path to custom front design image
    private String backDesignUrl; // Path to custom back design image (currently unused but kept for
                                  // compatibility)

    /**
     * Standard constructor for simple items.
     */
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Detailed constructor for customized items.
     */
    public CartItem(Product product, int quantity, String size, String hipsSize, String frontDesignUrl,
            String backDesignUrl) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.hipsSize = hipsSize;
        this.frontDesignUrl = frontDesignUrl;
        this.backDesignUrl = backDesignUrl;
    }

    // --- GETTERS & SETTERS ---

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHipsSize() {
        return hipsSize;
    }

    public void setHipsSize(String hipsSize) {
        this.hipsSize = hipsSize;
    }

    public String getFrontDesignUrl() {
        return frontDesignUrl;
    }

    public void setFrontDesignUrl(String frontDesignUrl) {
        this.frontDesignUrl = frontDesignUrl;
    }

    public String getBackDesignUrl() {
        return backDesignUrl;
    }

    public void setBackDesignUrl(String backDesignUrl) {
        this.backDesignUrl = backDesignUrl;
    }

    /**
     * Calculates total price for this specific cart line item.
     */
    public double getTotalPrice() {
        // Multiplies the base price by the quantity selected
        return product.getPrice() * quantity;
    }
}
