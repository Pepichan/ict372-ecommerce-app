package com.example.ict372ecommerceapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "CartPreferences";
    private static final String CART_KEY = "cart_items";

    private CartManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        cartItems = new ArrayList<>();
        loadCart();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }

    // Add item to cart or update quantity if it already exists
    public void addToCart(int productId, String productName, String productPrice,
                          String productImageUrl, String selectedColor, int quantity) {
        // Check if item with same product and color already exists
        CartItem existingItem = findItem(productId, selectedColor);

        if (existingItem != null) {
            // Update quantity
            existingItem.quantity += quantity;
        } else {
            // Add new item
            CartItem newItem = new CartItem(productId, productName, productPrice,
                    productImageUrl, selectedColor, quantity);
            cartItems.add(newItem);
        }

        saveCart();
    }

    // Remove item from cart
    public void removeItem(CartItem item) {
        cartItems.remove(item);
        saveCart();
    }

    // Update item quantity
    public void updateQuantity(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(item);
        } else {
            item.quantity = newQuantity;
            saveCart();
        }
    }

    // Get all cart items
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    // Get cart item count
    public int getItemCount() {
        return cartItems.size();
    }

    // Get total quantity of all items
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.quantity;
        }
        return total;
    }

    // Calculate cart total
    public double getTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Format total as string
    public String getTotalFormatted() {
        return String.format("$%.2f", getTotal());
    }

    // Clear cart
    public void clearCart() {
        cartItems.clear();
        saveCart();
    }

    // Find item by product ID and color
    private CartItem findItem(int productId, String color) {
        for (CartItem item : cartItems) {
            if (item.productId == productId && item.selectedColor.equals(color)) {
                return item;
            }
        }
        return null;
    }

    // Save cart to SharedPreferences
    private void saveCart() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (CartItem item : cartItems) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productId", item.productId);
                jsonObject.put("productName", item.productName);
                jsonObject.put("productPrice", item.productPrice);
                jsonObject.put("productImageUrl", item.productImageUrl);
                jsonObject.put("selectedColor", item.selectedColor);
                jsonObject.put("quantity", item.quantity);
                jsonArray.put(jsonObject);
            }

            prefs.edit().putString(CART_KEY, jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Load cart from SharedPreferences
    private void loadCart() {
        try {
            String cartJson = prefs.getString(CART_KEY, "[]");
            JSONArray jsonArray = new JSONArray(cartJson);

            cartItems.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                CartItem item = new CartItem(
                        jsonObject.getInt("productId"),
                        jsonObject.getString("productName"),
                        jsonObject.getString("productPrice"),
                        jsonObject.getString("productImageUrl"),
                        jsonObject.getString("selectedColor"),
                        jsonObject.getInt("quantity")
                );
                cartItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            cartItems.clear();
        }
    }
}
