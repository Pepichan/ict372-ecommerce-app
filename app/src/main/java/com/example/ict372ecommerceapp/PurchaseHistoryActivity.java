package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView rvOrders;
    private LinearLayout emptyState;
    private View cardStartShopping;

    private OrderDatabaseHelper orderDbHelper;
    private OrderAdapter orderAdapter;
    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        orderDbHelper = new OrderDatabaseHelper(this);

        initializeViews();
        setupRecyclerView();
        loadOrders();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        rvOrders = findViewById(R.id.rvOrders);
        emptyState = findViewById(R.id.emptyState);
        cardStartShopping = findViewById(R.id.cardStartShopping);
    }

    private void setupRecyclerView() {
        orders = new ArrayList<>();
        orderAdapter = new OrderAdapter(orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        // Get user ID from SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = pref.getInt("userId", -1);

        // Load orders
        List<Order> loadedOrders;
        if (userId != -1) {
            loadedOrders = orderDbHelper.getOrdersByUserId(userId);
        } else {
            // If no user ID, load all orders (fallback)
            loadedOrders = orderDbHelper.getAllOrders();
        }

        orders.clear();
        orders.addAll(loadedOrders);
        orderAdapter.notifyDataSetChanged();

        // Show/hide empty state
        if (orders.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardStartShopping.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseHistoryActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        View btnStartShopping = findViewById(R.id.btnStartShopping);
        if (btnStartShopping != null) {
            btnStartShopping.setOnClickListener(v -> {
                Intent intent = new Intent(PurchaseHistoryActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
