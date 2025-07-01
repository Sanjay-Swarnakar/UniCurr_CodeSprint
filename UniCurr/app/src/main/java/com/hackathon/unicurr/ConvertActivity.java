package com.hackathon.unicurr;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ConvertActivity extends AppCompatActivity {

    EditText edtAmount;
    Spinner spinnerFrom, spinnerTo;
    Button btnConvert;
    TextView txtResult;

    // Example rates (you can later fetch these from an API)
    Map<String, Double> currencyRates = new HashMap<>();
    Map<String, Double> pegWeights = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        edtAmount = findViewById(R.id.edtAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnConvert = findViewById(R.id.btnConvert);
        txtResult = findViewById(R.id.txtResult);

        String[] currencies = {"UniCurr", "USD", "EUR", "INR", "JPY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Hardcoded exchange rates for example (you’ll replace with API later)
        currencyRates.put("USD", 1.0);
        currencyRates.put("EUR", 1.1);
        currencyRates.put("INR", 0.012);
        currencyRates.put("JPY", 0.007);
        currencyRates.put("GBP", 1.3);

        // Weighted Peg Basket
        pegWeights.put("USD", 0.4);
        pegWeights.put("EUR", 0.3);
        pegWeights.put("JPY", 0.2);
        pegWeights.put("GBP", 0.1);

        btnConvert.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(edtAmount.getText().toString());
                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();

                double result = convertCurrency(amount, from, to);
                txtResult.setText(String.format("%.2f %s", result, to));
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    double getUniCurrRate() {
        double weightedSum = 0.0;
        for (String currency : pegWeights.keySet()) {
            double rate = currencyRates.getOrDefault(currency, 1.0);
            double weight = pegWeights.get(currency);
            weightedSum += rate * weight;
        }
        return weightedSum;
    }

    double convertCurrency(double amount, String from, String to) {
        double uniCurrValue = getUniCurrRate();

        if (from.equals("UniCurr")) {
            return amount * currencyRates.get(to) / uniCurrValue;
        } else if (to.equals("UniCurr")) {
            return amount * uniCurrValue / currencyRates.get(from);
        } else {
            // regular currency to currency
            return amount * currencyRates.get(to) / currencyRates.get(from);
        }
    }
}
