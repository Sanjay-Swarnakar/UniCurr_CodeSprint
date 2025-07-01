package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class SendMoneyActivity extends AppCompatActivity {

    EditText edtRecipientEmail, edtAmount;
    Button btnSend, btnScanQR;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        edtRecipientEmail = findViewById(R.id.edtRecipientEmail);
        edtAmount = findViewById(R.id.edtAmount);
        btnSend = findViewById(R.id.btnSend);
        btnScanQR = findViewById(R.id.btnScanQR);  // Add this button to your XML

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(v -> {
            btnSend.setEnabled(false); // prevent rapid clicks
            sendMoney();
        });

        btnScanQR.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan recipient QR");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            qrLauncher.launch(options);
        });
    }

    private final ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String scannedUid = result.getContents();
                    resolveUidToEmail(scannedUid);
                }
            });

    private void resolveUidToEmail(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String email = documentSnapshot.getString("email");
                    if (email != null) {
                        edtRecipientEmail.setText(email);
                        Toast.makeText(this, "Recipient loaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Invalid QR data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error resolving QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendMoney() {
        String recipientEmail = edtRecipientEmail.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().trim();

        if (recipientEmail.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            btnSend.setEnabled(true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            btnSend.setEnabled(true);
            return;
        }

        if (amount <= 0) {
            Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show();
            btnSend.setEnabled(true);
            return;
        }

        String senderUid = mAuth.getUid();
        DocumentReference senderRef = db.collection("users").document(senderUid);

        senderRef.get().addOnSuccessListener(senderSnap -> {
            Double senderBalance = senderSnap.getDouble("balance");
            if (senderBalance == null) senderBalance = 0.0;

            if (senderBalance < amount) {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                btnSend.setEnabled(true);
                return;
            }

            db.collection("users").whereEqualTo("email", recipientEmail).get()
                    .addOnSuccessListener(query -> {
                        if (query.isEmpty()) {
                            Toast.makeText(this, "Recipient not found", Toast.LENGTH_SHORT).show();
                            btnSend.setEnabled(true);
                            return;
                        }

                        DocumentSnapshot recipientSnap = query.getDocuments().get(0);
                        String recipientUid = recipientSnap.getId();
                        DocumentReference recipientRef = db.collection("users").document(recipientUid);

                        db.runTransaction((Transaction.Function<Void>) transaction -> {
                            DocumentSnapshot senderDoc = transaction.get(senderRef);
                            double sBalance = senderDoc.getDouble("balance") != null ? senderDoc.getDouble("balance") : 0.0;

                            if (sBalance < amount) {
                                throw new FirebaseFirestoreException("Insufficient funds",
                                        FirebaseFirestoreException.Code.ABORTED);
                            }

                            DocumentSnapshot rDoc = transaction.get(recipientRef);
                            double rBalance = rDoc.getDouble("balance") != null ? rDoc.getDouble("balance") : 0.0;

                            transaction.update(senderRef, "balance", sBalance - amount);
                            transaction.update(recipientRef, "balance", rBalance + amount);

                            return null;
                        }).addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Money sent successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Transaction failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            btnSend.setEnabled(true);
                        });

                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Recipient lookup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnSend.setEnabled(true);
                    });

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch sender: " + e.getMessage(), Toast.LENGTH_LONG).show();
            btnSend.setEnabled(true);
        });
    }
}
