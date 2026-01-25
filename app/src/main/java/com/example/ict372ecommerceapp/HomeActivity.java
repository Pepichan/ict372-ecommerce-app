package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //displays the XML layout

        RecyclerView rv = findViewById(R.id.rvProducts); //gets UI components by id
        rv.setLayoutManager(new GridLayoutManager(this, 2)); //displays products in a 2-column grid

        ArrayList<Product> data = new ArrayList<>(); //creates sample product data

        // Enhanced product data with descriptions, colors, and IDs
        // To add images: place image files in res/drawable folder
        // Example: product_linen_chair.png, product_pearl_lamp.png, etc.
        // Then use: R.drawable.product_linen_chair instead of 0

        data.add(new Product(
            1,
            "Linen Chair",
            "$321.00",
            "Crafted with premium linen and plush cushioning, this chair offers timeless style and ultimate relaxation.",
            new String[]{"#B8B8B8", "#6B4423"},
            new int[]{R.drawable.linenchair, R.drawable.brownlinenchair}
        ));

        data.add(new Product(
            2,
            "Pearl Lamp",
            "$191.00",
            "Elegant pearl-finish lamp that adds a soft, ambient glow to any room. Perfect for modern interiors.",
            new String[]{"#F5F5DC", "#FFD700"},
            new int[]{R.drawable.lamp, R.drawable.goldlamp}
        ));

        data.add(new Product(
            3,
            "Modern Chair",
            "$120.00",
            "Sleek and contemporary design with ergonomic support. Ideal for home offices and dining areas.",
            new String[]{"#F5F5DC", "#000000"},
            new int[]{R.drawable.modernchair, R.drawable.blackmodernchair}
        ));

        data.add(new Product(
            4,
            "Wood Table",
            "$450.00",
            "Handcrafted solid wood table with natural grain patterns. Durable and timeless piece for any dining space.",
            new String[]{"#8B4513", "#D2691E"},
            new int[]{R.drawable.woodetable, R.drawable.chocolatewoodtable}
        ));

        data.add(new Product(
            5,
            "Velvet Sofa",
            "$899.00",
            "Luxurious velvet sofa with deep seating and soft cushions. A statement piece for your living room.",
            new String[]{"#1E3A5F", "#4A4A4A"},
            new int[]{R.drawable.velvetsofa, R.drawable.grayvelvetsofa}
        ));

        data.add(new Product(
            6,
            "Bookshelf",
            "$275.00",
            "Modern open bookshelf with clean lines and sturdy shelves. Perfect for displaying books and decor.",
            new String[]{"#8B4513", "#000000"},
            new int[]{R.drawable.bookshelf, R.drawable.blackbookshelf}
        ));

        data.add(new Product(
            7,
            "Floor Mirror",
            "$189.00",
            "Full-length floor mirror with elegant frame. Adds light and depth to any room.",
            new String[]{"#000000", "#FFD700"},
            new int[]{R.drawable.floormirror, R.drawable.goldfloormirror}
        ));

        data.add(new Product(
            8,
            "Coffee Table",
            "$320.00",
            "Minimalist coffee table with tempered glass top and wooden legs. Stylish centerpiece for your living space.",
            new String[]{"#8B4513", "#000000"},
            new int[]{R.drawable.coffeetable, R.drawable.blackcoffetable}
        ));

        // ✅ sets up the RecyclerView with the ProductAdapter and click listener
        ProductAdapter adapter = new ProductAdapter(data, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.id);
            intent.putExtra("PRODUCT_NAME", product.name);
            intent.putExtra("PRODUCT_PRICE", product.price);
            intent.putExtra("PRODUCT_DESCRIPTION", product.description);
            intent.putExtra("PRODUCT_IMAGE_RES_ID", product.imageResId);
            intent.putExtra("PRODUCT_COLORS", product.colors);
            intent.putExtra("PRODUCT_COLOR_IMAGES", product.colorImageResIds);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        // Profile button click listener - shows popup menu
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_profile) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
                } else if (id == R.id.menu_cart) {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    return true;
                } else if (id == R.id.menu_orders) {
                    startActivity(new Intent(HomeActivity.this, PurchaseHistoryActivity.class));
                    return true;
                } else if (id == R.id.menu_logout) {
                    showLogoutDialog();
                    return true;
                }
                return false;
            });

            popup.show();
        });

        AutoCompleteTextView etSearch = findViewById(R.id.etSearch); //gets the search input field


        // ✅ typed-ahead suggestions
        String[] suggestions = new String[]{"Linen Chair", "Pearl Lamp", "Modern Chair", "Wood Table", "Velvet Sofa", "Bookshelf", "Floor Mirror", "Coffee Table"};
        ArrayAdapter<String> sugAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions
        );
        etSearch.setAdapter(sugAdapter); //sets the adapter for suggestions


        // ✅ Filter by input
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        // ✅ Filter by selecting a suggestion
        etSearch.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            etSearch.dismissDropDown();         //hides the suggestions dropdown
            adapter.filter(selected);           //narrowing the list to the selected suggestion
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes", (dialog, which) -> {
                // Clear session
                SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.remove("userId");
                editor.remove("userName");
                editor.remove("userEmail");
                editor.apply();

                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Go to Login screen
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("No", null)
            .show();
    }
}
