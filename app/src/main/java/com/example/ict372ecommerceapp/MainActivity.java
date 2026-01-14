package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ For testing: go t o Home after launching the app
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }
}
