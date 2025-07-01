package com.hackathon.unicurr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView txtUsername, txtBalance;
    Button btnLogout;

    private double currentBalance = 500.0; // Mock balance
    private String userEmail = "johndoe@example.com"; // Mock user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsername = findViewById(R.id.txtUsername);
        txtBalance = findViewById(R.id.txtBalance);
        btnLogout = findViewById(R.id.btnLogout);

        txtUsername.setText("User: " + userEmail);
        txtBalance.setText("Balance: " + currentBalance + " UniCurr");

        btnLogout.setOnClickListener(v -> {
            // Future: Clear Firebase auth
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
}
