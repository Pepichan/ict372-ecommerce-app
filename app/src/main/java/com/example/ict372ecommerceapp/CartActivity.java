package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private TextView tvCartTitle;
    private ImageButton btnBack;
    private RecyclerView rvCartItems;
    private TextView tvSubtotal;
    private TextView tvTotal;
    private TextView btnCheckout;

    private CartManager cartManager;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupCart();
        setupListeners();
    }

    private void initializeViews() {
        tvCartTitle = findViewById(R.id.tvCartTitle);
        btnBack = findViewById(R.id.btnBack);
        rvCartItems = findViewById(R.id.rvCartItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupCart() {
        cartManager = CartManager.getInstance(this);

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        List<CartItem> items = cartManager.getCartItems();
        cartAdapter = new CartAdapter(items);
        cartAdapter.setOnCartItemChangeListener(this);
        rvCartItems.setAdapter(cartAdapter);

        updateCartDisplay();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCheckout.setOnClickListener(v -> {
            if (cartManager.getItemCount() == 0) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to checkout activity
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateCartDisplay() {
        // Update title with item count
        int itemCount = cartManager.getItemCount();
        tvCartTitle.setText("My cart (" + itemCount + ")");

        // Update totals
        String totalFormatted = cartManager.getTotalFormatted();
        tvSubtotal.setText(totalFormatted);
        tvTotal.setText(totalFormatted);

        // Update RecyclerView
        cartAdapter.updateItems(cartManager.getCartItems());
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        cartManager.updateQuantity(item, newQuantity);
        updateCartDisplay();

        if (newQuantity <= 0) {
            Toast.makeText(this, item.productName + " removed from cart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemDeleted(CartItem item) {
        cartManager.removeItem(item);
        updateCartDisplay();
        Toast.makeText(this, item.productName + " removed from cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cart when returning to this activity
        updateCartDisplay();
    }
}
