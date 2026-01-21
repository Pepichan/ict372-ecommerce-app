package com.example.ict372ecommerceapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();

                // Validation logic
                if (name.isEmpty() || email.isEmpty() || pass.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields. Password min 6 chars.", Toast.LENGTH_SHORT).show();
                } else {
                    // Storing data in SharedPreferences (Basic User Data Persistence)
                    SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("password", pass);
                    editor.apply();

                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to login
                }
            }
        });
    }
}