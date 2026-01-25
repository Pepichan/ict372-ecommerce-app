package com.example.ict372ecommerceapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageButton btnBack, btnCart;
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private LinearLayout colorOptionsContainer;
    private CardView btnDecreaseQuantity;
    private TextView tvQuantity;
    private CardView btnIncreaseQuantity;
    private View cardAddToCart;

    private Product product;
    private String selectedColor = "";
    private int selectedColorIndex = 0;
    private int quantity = 1;
    private int[] colorImageResIds;

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
        btnCart = findViewById(R.id.btnCart);
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        colorOptionsContainer = findViewById(R.id.colorOptionsContainer);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        cardAddToCart = findViewById(R.id.cardAddToCart);

        // Also get the text button inside the card
        TextView btnAddToCartText = findViewById(R.id.btnAddToCart);
        if (btnAddToCartText != null) {
            btnAddToCartText.setOnClickListener(v -> addToCart());
        }
    }

    private void loadProductData() {
        // Get product data from intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String productPrice = getIntent().getStringExtra("PRODUCT_PRICE");
        String productDescription = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        int productImageResId = getIntent().getIntExtra("PRODUCT_IMAGE_RES_ID", 0);
        String[] productColors = getIntent().getStringArrayExtra("PRODUCT_COLORS");
        colorImageResIds = getIntent().getIntArrayExtra("PRODUCT_COLOR_IMAGES");

        // Create product object
        product = new Product(productId, productName, productPrice, productDescription, productImageResId, productColors);

        // Display product data
        tvProductName.setText(product.name);
        tvProductDescription.setText(product.description);
        tvProductPrice.setText(product.price);

        // Set product image
        if (product.imageResId != 0) {
            imgProduct.setImageResource(product.imageResId);
        }

        // Create color options
        if (product.colors != null && product.colors.length > 0) {
            createColorOptions(product.colors);
            selectedColor = product.colors[0]; // Set first color as default
        }
    }

    private void createColorOptions(String[] colors) {
        colorOptionsContainer.removeAllViews();

        for (int i = 0; i < colors.length; i++) {
            final String color = colors[i];
            final int colorIndex = i;

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
                selectColor(color, colorIndex);
                updateColorSelection(colorView);
                // Update product image based on selected color
                if (colorImageResIds != null && colorIndex < colorImageResIds.length) {
                    imgProduct.setImageResource(colorImageResIds[colorIndex]);
                }
            });

            // Select first color by default
            if (i == 0) {
                selectedColor = color;
                drawable.setStroke(4, Color.BLACK);
            }

            colorOptionsContainer.addView(colorView);
        }
    }

    private void selectColor(String color, int index) {
        selectedColor = color;
        selectedColorIndex = index;
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

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnDecreaseQuantity.setOnClickListener(v -> decreaseQuantity());

        btnIncreaseQuantity.setOnClickListener(v -> increaseQuantity());

        cardAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        // Get the correct image for the selected color
        int imageToUse = product.imageResId;
        if (colorImageResIds != null && selectedColorIndex < colorImageResIds.length) {
            imageToUse = colorImageResIds[selectedColorIndex];
        }

        // Add item to cart using CartManager
        CartManager cartManager = CartManager.getInstance(this);
        cartManager.addToCart(
                product.id,
                product.name,
                product.price,
                imageToUse,
                selectedColor != null ? selectedColor : "",
                quantity
        );

        // Show Snackbar with "Continue Shopping" action
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, quantity + "x " + product.name + " added to cart!", Snackbar.LENGTH_LONG)
                .setAction("Continue Shopping", view -> finish())
                .show();

        // Reset quantity to 1 after adding to cart
        quantity = 1;
        updateQuantityDisplay();
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
