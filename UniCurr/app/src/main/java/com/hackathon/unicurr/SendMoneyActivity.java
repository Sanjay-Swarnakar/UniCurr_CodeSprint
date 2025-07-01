package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SendMoneyActivity extends AppCompatActivity {

    private EditText inputRecipient, inputAmount;
    private TextView sendResult;

    private double currentBalance = 500.0; // Simulated balance (can be dynamic later)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        inputRecipient = findViewById(R.id.inputRecipient);
        inputAmount = findViewById(R.id.inputAmount);
        sendResult = findViewById(R.id.sendResult);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> sendMoney());
    }

    private void sendMoney() {
        String recipient = inputRecipient.getText().toString().trim();
        String amountStr = inputAmount.getText().toString().trim();

        if (recipient.isEmpty() || amountStr.isEmpty()) {
            sendResult.setText("Please enter recipient and amount.");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (amount <= 0) {
            sendResult.setText("Amount must be greater than 0.");
            return;
        }

        if (amount > currentBalance) {
            sendResult.setText("Insufficient balance.");
            return;
        }

        // Simulate sending
        currentBalance -= amount;

        sendResult.setText("✅ Sent UniCurr " + amount + " to " + recipient + "\nNew Balance: " + currentBalance);
    }
}

