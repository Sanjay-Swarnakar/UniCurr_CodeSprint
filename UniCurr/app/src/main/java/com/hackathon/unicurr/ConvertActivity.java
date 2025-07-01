package com.hackathon.unicurr;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class ConvertActivity extends AppCompatActivity {

    private EditText amountInput;
    private Spinner spinnerDirection;
    private TextView resultText;

    private final String[] directions = {
            "UniCurr → USD", "USD → UniCurr",
            "UniCurr → EUR", "EUR → UniCurr",
            "UniCurr → JPY", "JPY → UniCurr",
            "UniCurr → GBP", "GBP → UniCurr"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        amountInput = findViewById(R.id.amountInput);
        spinnerDirection = findViewById(R.id.spinnerDirection);
        resultText = findViewById(R.id.resultText);
        Button btnConvertNow = findViewById(R.id.btnConvertNow);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, directions);
        spinnerDirection.setAdapter(adapter);

        btnConvertNow.setOnClickListener(v -> convertCurrency());
    }

    private void convertCurrency() {
        String inputStr = amountInput.getText().toString();
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(inputStr);
        String selected = spinnerDirection.getSelectedItem().toString();

        CurrencyAPI.fetchRates(new CurrencyAPI.Callback() {
            @Override
            public void onSuccess(Map<String, Double> rates) {
                double unicurrRate = BasketCurrencies.calculateUniCurr(rates);
                double result = 0;

                switch (selected) {
                    case "UniCurr → USD":
                        result = amount * rates.get("USD") / unicurrRate;
                        break;
                    case "USD → UniCurr":
                        result = amount * unicurrRate / rates.get("USD");
                        break;
                    case "UniCurr → EUR":
                        result = amount * rates.get("EUR") / unicurrRate;
                        break;
                    case "EUR → UniCurr":
                        result = amount * unicurrRate / rates.get("EUR");
                        break;
                    case "UniCurr → JPY":
                        result = amount * rates.get("JPY") / unicurrRate;
                        break;
                    case "JPY → UniCurr":
                        result = amount * unicurrRate / rates.get("JPY");
                        break;
                    case "UniCurr → GBP":
                        result = amount * rates.get("GBP") / unicurrRate;
                        break;
                    case "GBP → UniCurr":
                        result = amount * unicurrRate / rates.get("GBP");
                        break;
                }

                double finalResult = result;
                runOnUiThread(() -> resultText.setText(String.format("Converted: %.4f", finalResult)));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> resultText.setText("Error: " + e.getMessage()));
            }
        });
    }
}
