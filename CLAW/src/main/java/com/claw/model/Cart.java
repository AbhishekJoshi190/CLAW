package com.claw.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    // List to store all items currently in the cart
    private List<CartItem> items;

    /**
     * Constructor initializes an empty list for the cart items.
     */
    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a standard product to the cart.
     * If the item already exists, it increases the quantity.
     */
    public void addItem(Product product) {
        if (product == null || product.getId() == null) {
            return; // Safety check: don't add null products
        }

        // Check if product is already in the cart to avoid duplicates
        for (CartItem item : items) {
            if (item.getProduct() != null &&
                    item.getProduct().getId() != null &&
                    product.getId().equals(item.getProduct().getId())) {

                // Item found: increment quantity and exit
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        // New item: add it to the list with quantity 1
        items.add(new CartItem(product, 1));
    }

    /**
     * Adds a product to the cart. If the user adds the exact same item configuration 
     * (same product ID, size, custom fits, and design graphics), we simply increment 
     * the quantity rather than cluttering their shopping bag with duplicate rows.
     */
    public void addCustomItem(CartItem newItem) {
        // Quick guard clause: ignore null products to avoid crashes
        if (newItem == null || newItem.getProduct() == null || newItem.getProduct().getId() == null) {
            return;
        }

        // Let's scan through the existing bag items to look for an identical match
        for (CartItem existingItem : items) {
            if (existingItem.getProduct() != null &&
                    newItem.getProduct().getId().equals(existingItem.getProduct().getId()) &&
                    isSameString(newItem.getSize(), existingItem.getSize()) &&
                    isSameString(newItem.getHipsSize(), existingItem.getHipsSize()) &&
                    isSameString(newItem.getFrontDesignUrl(), existingItem.getFrontDesignUrl()) &&
                    isSameString(newItem.getBackDesignUrl(), existingItem.getBackDesignUrl())) {

                // Found a perfect duplicate! Let's just combine the quantities and exit.
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                return;
            }
        }

        // If it's a completely new product size/design configuration, append it as a new row.
        items.add(newItem);
    }

    /**
     * Safe string comparison helper. Handles nulls, spaces, and blank fields 
     * cleanly so we never run into annoying NullPointerExceptions.
     */
    private boolean isSameString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty()) {
            return s2 == null || s2.trim().isEmpty();
        }
        return s1.equals(s2);
    }

    /**
     * Returns the full list of items in the cart.
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Calculates the total price of all items combined.
     */
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            if (item != null) {
                total += item.getTotalPrice();
            }
        }
        return total;
    }

    /**
     * Utility to check if the cart is empty.
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    /**
     * Removes an item from the cart based on its position (index) in the list.
     */
    public void removeByIndex(int index) {
        // Safety check: ensure index is within valid range
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
}