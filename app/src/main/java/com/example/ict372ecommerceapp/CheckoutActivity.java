package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etAddress, etCity, etPostalCode;
    private RadioGroup rgPaymentMethod;
    private TextView tvItemCount, tvSubtotal, tvTotal;
    private View cardPlaceOrder;
    private TextView btnPlaceOrder;

    private CartManager cartManager;
    private OrderDatabaseHelper orderDbHelper;
    private double totalAmount;
    private String userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartManager = CartManager.getInstance(this);
        orderDbHelper = new OrderDatabaseHelper(this);

        initializeViews();
        loadOrderSummary();
        loadUserData();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etPostalCode = findViewById(R.id.etPostalCode);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
        cardPlaceOrder = findViewById(R.id.cardPlaceOrder);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    private void loadOrderSummary() {
        int itemCount = cartManager.getTotalQuantity();
        totalAmount = cartManager.getTotal();

        tvItemCount.setText(itemCount + " items");
        tvSubtotal.setText(cartManager.getTotalFormatted());
        tvTotal.setText(cartManager.getTotalFormatted());
    }

    private void loadUserData() {
        // Get logged in user data
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userName = pref.getString("userName", "");
        userEmail = pref.getString("userEmail", "");
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardPlaceOrder.setOnClickListener(v -> placeOrder());
        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        if (!validateForm()) {
            return;
        }

        // Get form data
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String postalCode = etPostalCode.getText().toString().trim();
        String paymentMethod = getSelectedPaymentMethod();

        // Get user ID
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = pref.getInt("userId", -1);

        // Get current date
        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // Convert cart items to JSON
        String itemsJson = cartItemsToJson();

        // Create order
        Order order = new Order(
                userId,
                userName,
                userEmail,
                address,
                city,
                postalCode,
                "",  // phone not collected
                paymentMethod,
                orderDate,
                totalAmount,
                "Pending",
                itemsJson
        );

        // Save order to database
        long orderId = orderDbHelper.createOrder(order);

        if (orderId != -1) {
            // Clear cart
            cartManager.clearCart();

            // Show success dialog
            showOrderSuccessDialog(orderId);
        } else {
            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String postalCode = etPostalCode.getText().toString().trim();

        // Validate address
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }

        // Validate city
        if (city.isEmpty()) {
            etCity.setError("City is required");
            etCity.requestFocus();
            return false;
        }

        // Validate postal code
        if (postalCode.isEmpty()) {
            etPostalCode.setError("Postal code is required");
            etPostalCode.requestFocus();
            return false;
        }

        // Validate cart is not empty
        if (cartManager.getItemCount() == 0) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == R.id.rbCreditCard) {
            return "Credit Card";
        } else if (selectedId == R.id.rbDebitCard) {
            return "Debit Card";
        } else if (selectedId == R.id.rbPaypal) {
            return "PayPal";
        } else if (selectedId == R.id.rbCashOnDelivery) {
            return "Cash on Delivery";
        }
        return "Credit Card";
    }

    private String cartItemsToJson() {
        try {
            JSONArray jsonArray = new JSONArray();
            List<CartItem> items = cartManager.getCartItems();

            for (CartItem item : items) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productId", item.productId);
                jsonObject.put("productName", item.productName);
                jsonObject.put("productPrice", item.productPrice);
                jsonObject.put("quantity", item.quantity);
                jsonObject.put("selectedColor", item.selectedColor);
                jsonArray.put(jsonObject);
            }

            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private void showOrderSuccessDialog(long orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Order Placed!")
                .setMessage("Your order #" + orderId + " has been placed successfully.\n\nThank you for shopping with us!")
                .setPositiveButton("View Orders", (dialog, which) -> {
                    Intent intent = new Intent(CheckoutActivity.this, PurchaseHistoryActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Continue Shopping", (dialog, which) -> {
                    Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
