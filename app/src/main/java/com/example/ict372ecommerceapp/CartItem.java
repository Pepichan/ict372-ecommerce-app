package com.example.ict372ecommerceapp;

public class CartItem {
    public int productId;
    public String productName;
    public String productPrice;
    public String productImageUrl;
    public String selectedColor;
    public int quantity;

    public CartItem(int productId, String productName, String productPrice,
                    String productImageUrl, String selectedColor, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.selectedColor = selectedColor;
        this.quantity = quantity;
    }

    // Get price as double (removing $ sign)
    public double getPriceValue() {
        try {
            return Double.parseDouble(productPrice.replace("$", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Get subtotal for this item
    public double getSubtotal() {
        return getPriceValue() * quantity;
    }

    // Format subtotal as string
    public String getSubtotalFormatted() {
        return String.format("$%.2f", getSubtotal());
    }
}
