package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.Objects;

public class BalanceActivity extends AppCompatActivity {

    TextView txtBalance;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        txtBalance = findViewById(R.id.txtBalance);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fetchBalance();
    }

    private void fetchBalance() {
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Double balance = documentSnapshot.getDouble("balance");
                if (balance == null) balance = 0.0;
                txtBalance.setText(String.format("Your UniCurr Balance: %.2f", balance));
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to fetch balance: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}
