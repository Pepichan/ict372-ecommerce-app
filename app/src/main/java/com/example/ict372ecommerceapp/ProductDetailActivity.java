package com.example.ict372ecommerceapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private LinearLayout colorOptionsContainer;
    private CardView btnDecreaseQuantity;
    private TextView tvQuantity;
    private CardView btnIncreaseQuantity;
    private TextView btnAddToCart;

    private Product product;
    private String selectedColor = null;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        loadProductData();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        colorOptionsContainer = findViewById(R.id.colorOptionsContainer);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void loadProductData() {
        // Get product data from intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String productPrice = getIntent().getStringExtra("PRODUCT_PRICE");
        String productDescription = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        String productImageUrl = getIntent().getStringExtra("PRODUCT_IMAGE_URL");
        String[] productColors = getIntent().getStringArrayExtra("PRODUCT_COLORS");

        // Create product object
        product = new Product(productId, productName, productPrice, productDescription, productImageUrl, productColors);

        // Display product data
        tvProductName.setText(product.name);
        tvProductDescription.setText(product.description);
        tvProductPrice.setText(product.price);

        // For now, using placeholder image (gray background)
        // In future, you can use Glide or Picasso to load images from URL
        // Glide.with(this).load(product.imageUrl).into(imgProduct);

        // Create color options
        if (product.colors != null && product.colors.length > 0) {
            createColorOptions(product.colors);
        }
    }

    private void createColorOptions(String[] colors) {
        colorOptionsContainer.removeAllViews();

        for (int i = 0; i < colors.length; i++) {
            final String color = colors[i];

            // Create circular color view
            View colorView = new View(this);
            int size = (int) (50 * getResources().getDisplayMetrics().density); // 50dp
            int margin = (int) (8 * getResources().getDisplayMetrics().density); // 8dp

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, 0, margin, 0);
            colorView.setLayoutParams(params);

            // Create circular drawable
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);

            try {
                drawable.setColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                // If color parsing fails, use a default gray
                drawable.setColor(Color.GRAY);
            }

            // Add stroke for unselected state
            drawable.setStroke(3, Color.parseColor("#E0E0E0"));

            colorView.setBackground(drawable);

            // Set click listener
            colorView.setOnClickListener(v -> {
                selectColor(color);
                updateColorSelection(colorView);
            });

            // Select first color by default
            if (i == 0) {
                selectedColor = color;
                drawable.setStroke(4, Color.BLACK);
            }

            colorOptionsContainer.addView(colorView);
        }
    }

    private void selectColor(String color) {
        selectedColor = color;
    }

    private void updateColorSelection(View selectedView) {
        // Reset all color views
        for (int i = 0; i < colorOptionsContainer.getChildCount(); i++) {
            View colorView = colorOptionsContainer.getChildAt(i);
            GradientDrawable drawable = (GradientDrawable) colorView.getBackground();
            drawable.setStroke(3, Color.parseColor("#E0E0E0"));
        }

        // Highlight selected color
        GradientDrawable selectedDrawable = (GradientDrawable) selectedView.getBackground();
        selectedDrawable.setStroke(4, Color.BLACK);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnDecreaseQuantity.setOnClickListener(v -> decreaseQuantity());

        btnIncreaseQuantity.setOnClickListener(v -> increaseQuantity());

        btnAddToCart.setOnClickListener(v -> {
            // Add item to cart using CartManager
            CartManager cartManager = CartManager.getInstance(this);
            cartManager.addToCart(
                    product.id,
                    product.name,
                    product.price,
                    product.imageUrl,
                    selectedColor,
                    quantity
            );

            // Show Snackbar with "Continue Shopping" action
            Snackbar.make(v, quantity + "x " + product.name + " added to cart!", Snackbar.LENGTH_LONG)
                    .setAction("Continue Shopping", view -> finish())
                    .show();

            // Reset quantity to 1 after adding to cart
            quantity = 1;
            updateQuantityDisplay();
        });
    }

    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityDisplay();
        }
    }

    private void increaseQuantity() {
        quantity++;
        updateQuantityDisplay();
    }

    private void updateQuantityDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
    }
}
