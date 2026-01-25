package com.example.ict372ecommerceapp;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RegistrationActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button btnRegister;
    ImageButton btnBack;
    TextView tvGoToLogin, tvPrivacyPolicy;
    CheckBox cbPrivacyPolicy, cbPromotionalEmails;
    DatabaseHelper dbHelper;

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
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        cbPrivacyPolicy = findViewById(R.id.cbPrivacyPolicy);
        cbPromotionalEmails = findViewById(R.id.cbPromotionalEmails);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Go to login link
        tvGoToLogin.setOnClickListener(v -> finish());

        // Setup Privacy Policy clickable text
        setupPrivacyPolicyText();

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

                if (pass.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!cbPrivacyPolicy.isChecked()) {
                    Toast.makeText(RegistrationActivity.this, "Please agree to the Privacy Policy and Terms of Service", Toast.LENGTH_SHORT).show();
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

    private void setupPrivacyPolicyText() {
        String fullText = "I agree to the Privacy Policy and Terms of Service";
        String clickablePart = "Privacy Policy and Terms of Service";

        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf(clickablePart);
        int endIndex = startIndex + clickablePart.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showPrivacyPolicyDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(RegistrationActivity.this, R.color.text_primary));
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvPrivacyPolicy.setText(spannableString);
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        tvPrivacyPolicy.setHighlightColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void showPrivacyPolicyDialog() {
        String termsAndConditions =
            "TERMS OF SERVICE AND PRIVACY POLICY\n\n" +
            "Last Updated: January 2025\n\n" +
            "1. ACCEPTANCE OF TERMS\n" +
            "By accessing and using this application, you accept and agree to be bound by the terms and provisions of this agreement.\n\n" +
            "2. USE OF THE APPLICATION\n" +
            "You agree to use this application only for lawful purposes and in a way that does not infringe the rights of others.\n\n" +
            "3. USER ACCOUNTS\n" +
            "You are responsible for maintaining the confidentiality of your account and password. You agree to accept responsibility for all activities that occur under your account.\n\n" +
            "4. PRIVACY POLICY\n" +
            "We collect and use your personal information to provide and improve our services. Your information is protected and will not be shared with third parties without your consent, except as required by law.\n\n" +
            "5. DATA COLLECTION\n" +
            "We may collect the following information:\n" +
            "- Name and email address\n" +
            "- Purchase history and preferences\n" +
            "- Device information and usage data\n\n" +
            "6. PAYMENT TERMS\n" +
            "All payments are processed securely. Prices are subject to change without notice.\n\n" +
            "7. LIMITATION OF LIABILITY\n" +
            "We shall not be liable for any indirect, incidental, special, or consequential damages resulting from the use of this application.\n\n" +
            "8. CHANGES TO TERMS\n" +
            "We reserve the right to modify these terms at any time. Continued use of the application constitutes acceptance of the modified terms.\n\n" +
            "9. CONTACT US\n" +
            "If you have any questions about these Terms, please contact us through the app.";

        new AlertDialog.Builder(this)
            .setTitle("Privacy Policy & Terms of Service")
            .setMessage(termsAndConditions)
            .setPositiveButton("I Understand", (dialog, which) -> dialog.dismiss())
            .show();
    }
}
