package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BalanceActivity extends AppCompatActivity {
    private double userBalance = 500.00; // Static for now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        TextView balanceText = findViewById(R.id.balanceText);
        balanceText.setText("Your Balance: UniCurr " + userBalance);
    }
}