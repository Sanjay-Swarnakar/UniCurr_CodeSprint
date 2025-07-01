package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.Objects;

public class SendMoneyActivity extends AppCompatActivity {

    EditText edtRecipientEmail, edtAmount;
    Button btnSend;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        edtRecipientEmail = findViewById(R.id.edtRecipientEmail);
        edtAmount = findViewById(R.id.edtAmount);
        btnSend = findViewById(R.id.btnSendMoney);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(v -> {
            String recipientEmail = edtRecipientEmail.getText().toString().trim();
            double amount;

            try {
                amount = Double.parseDouble(edtAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0 || recipientEmail.isEmpty()) {
                Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
                return;
            }

            sendMoneyToUser(recipientEmail, amount);
        });
    }

    private void sendMoneyToUser(String email, double amount) {
        String senderUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        // Step 1: Get sender balance
        db.collection("users").document(senderUid).get().addOnSuccessListener(senderDoc -> {
            double senderBalance = senderDoc.getDouble("balance") != null ? senderDoc.getDouble("balance") : 0;

            if (senderBalance < amount) {
                Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
                return;
            }

            // Step 2: Get recipient UID by email
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(this, "Recipient not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DocumentSnapshot recipientDoc = queryDocumentSnapshots.getDocuments().get(0);
                        String recipientUid = recipientDoc.getId();
                        double recipientBalance = recipientDoc.getDouble("balance") != null ? recipientDoc.getDouble("balance") : 0;

                        // Step 3: Perform balance update
                        db.runBatch(batch -> {
                            // Deduct from sender
                            batch.update(db.collection("users").document(senderUid), "balance", senderBalance - amount);
                            // Add to recipient
                            batch.update(db.collection("users").document(recipientUid), "balance", recipientBalance + amount);
                        }).addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Money sent!", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e ->
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );

                    });
        });
    }
}
