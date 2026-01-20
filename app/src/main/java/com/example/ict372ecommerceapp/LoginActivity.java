package com.example.ict372ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Handling: Check if user is already logged in
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (pref.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(LoginActivity.this, ProductdetailActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = etEmail.getText().toString();
                String passInput = etPassword.getText().toString();

                String savedEmail = pref.getString("email", "");
                String savedPass = pref.getString("password", "");

                if (emailInput.equals(savedEmail) && passInput.equals(savedPass)) {
                    // Update Session
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Navigation to Home Page
                    Intent intent = new Intent(LoginActivity.this, ProductdetailActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}