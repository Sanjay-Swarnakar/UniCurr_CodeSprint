package com.hackathon.unicurr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views and Firebase instances
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // ✅ Validate email and password
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Register with Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        String uid = mAuth.getCurrentUser().getUid();

                        // Create initial user profile in Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("balance", 100.0); // Starting UniCurr balance

                        db.collection("users").document(uid).set(userData)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                );
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });
    }
}
