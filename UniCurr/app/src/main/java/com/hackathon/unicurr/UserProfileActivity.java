package com.hackathon.unicurr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView txtWelcome, txtBalance;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtBalance = findViewById(R.id.txtBalance);
        btnLogout = findViewById(R.id.btnLogout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            txtWelcome.setText("Welcome, " + user.getEmail());
            // TODO: Load real balance from Firebase; for now, show mock balance
            txtBalance.setText("Balance: 100.0 UniCurr");
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });
    }
}
