package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductdetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        // ✅ Receive data from Home
        String name = getIntent().getStringExtra("product_name");
        String price = getIntent().getStringExtra("product_price");

        if (name != null) tvName.setText(name);
        if (price != null) tvPrice.setText(price);

        //  ✅ UI-only navigation to Cart
        btnAddToCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductdetailActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // TODO (Oussama):
        // - Add real Add to Cart logic here
    }
}