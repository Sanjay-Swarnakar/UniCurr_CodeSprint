package com.hackathon.unicurr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class UserProfileActivity extends AppCompatActivity {

    private TextView txtWelcome, txtBalance;
    private Button btnLogout;
    private ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtBalance = findViewById(R.id.txtBalance);
        btnLogout = findViewById(R.id.btnLogout);
        qrImage = findViewById(R.id.qrImage); // Make sure your XML has this ID

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();

            txtWelcome.setText("Welcome, " + email);
            txtBalance.setText("Balance: 100.0 UniCurr"); // TODO: Replace with Firestore fetch

            // Generate QR Code from UID
            try {
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.encodeBitmap(uid, BarcodeFormat.QR_CODE, 500, 500);
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            }
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });
    }
}
