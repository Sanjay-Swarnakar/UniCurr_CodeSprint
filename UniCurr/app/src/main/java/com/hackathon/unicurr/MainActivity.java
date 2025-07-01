package com.hackathon.unicurr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnBalance, btnConvert, btnSend, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBalance = findViewById(R.id.btnBalance);
        btnConvert = findViewById(R.id.btnConvert);
        btnSend = findViewById(R.id.btnSend);
        btnProfile = findViewById(R.id.btnProfile);

        btnBalance.setOnClickListener(v -> startActivity(new Intent(this, BalanceActivity.class)));
        btnConvert.setOnClickListener(v -> startActivity(new Intent(this, ConvertActivity.class)));
        btnSend.setOnClickListener(v -> startActivity(new Intent(this, SendMoneyActivity.class)));
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }
}