package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //displays the XML layout

        RecyclerView rv = findViewById(R.id.rvProducts); //gets UI components by id
        rv.setLayoutManager(new LinearLayoutManager(this)); //makes the RecyclerView a vertical scrolling list

        ArrayList<Product> data = new ArrayList<>(); //creates sample product data

        // Enhanced product data with descriptions, colors, and IDs
        data.add(new Product(
            1,
            "Linen Chair",
            "$321.00",
            "Crafted with premium linen and plush cushioning, this chair offers timeless style and ultimate relaxation.",
            "",
            new String[]{"#B8B8B8", "#6B4423", "#7B68A6", "#D4A373"}
        ));

        data.add(new Product(
            2,
            "Pearl Lamp",
            "$191.00",
            "Elegant pearl-finish lamp that adds a soft, ambient glow to any room. Perfect for modern interiors.",
            "",
            new String[]{"#F5F5DC", "#FFD700", "#C0C0C0"}
        ));

        data.add(new Product(
            3,
            "Modern Chair",
            "$120.00",
            "Sleek and contemporary design with ergonomic support. Ideal for home offices and dining areas.",
            "",
            new String[]{"#000000", "#FFFFFF", "#808080", "#8B4513"}
        ));

        data.add(new Product(
            4,
            "Wood Table",
            "$450.00",
            "Handcrafted solid wood table with natural grain patterns. Durable and timeless piece for any dining space.",
            "",
            new String[]{"#8B4513", "#D2691E", "#A0522D"}
        ));


        // ✅ sets up the RecyclerView with the ProductAdapter and click listener
        ProductAdapter adapter = new ProductAdapter(data, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.id);
            intent.putExtra("PRODUCT_NAME", product.name);
            intent.putExtra("PRODUCT_PRICE", product.price);
            intent.putExtra("PRODUCT_DESCRIPTION", product.description);
            intent.putExtra("PRODUCT_IMAGE_URL", product.imageUrl);
            intent.putExtra("PRODUCT_COLORS", product.colors);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        // Cart button click listener
        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        AutoCompleteTextView etSearch = findViewById(R.id.etSearch); //gets the search input field


        // ✅ typed-ahead suggestions
        String[] suggestions = new String[]{"Linen Chair", "Pearl Lamp", "Modern Chair", "Wood Table"};
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
}
