package com.example.ict372ecommerceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button btnRegister;
    ImageButton btnBack;
    TextView tvGoToLogin;
    DatabaseHelper dbHelper;
    CheckBox checkboxConsent;
    CheckBox checkboxOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        checkboxConsent = findViewById(R.id.checkboxConsent);
        checkboxOption = findViewById(R.id.checkboxOption);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Go to login link
        tvGoToLogin.setOnClickListener(v -> finish());

        // Register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();

                // Validation logic
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validate name length check
                if (name.length() < 6) {
                    Toast.makeText(RegistrationActivity.this,
                            "Name must be at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validate email format check
                if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(RegistrationActivity.this,
                            "Please enter a valid Gmail address",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if checkbox is checked
                if (!checkboxConsent.isChecked()) {
                    Toast.makeText(RegistrationActivity.this, "You must agree to the Privacy Policy to continue", Toast.LENGTH_SHORT).show();
                    return;

                }
                // Check if email already exists
                if (dbHelper.emailExists(email)) {
                    Toast.makeText(RegistrationActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user in SQLite database
                User user = new User(name, email, pass);
                boolean success = dbHelper.registerUser(user);

                if (success) {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to login
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
