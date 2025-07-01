package com.hackathon.unicurr;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ConvertActivity extends AppCompatActivity {

    EditText edtAmount;
    Spinner spinnerFrom, spinnerTo;
    TextView txtResult;
    Button btnConvert;

    // Static weights for UniCurr basket
    Map<String, Double> pegWeights = new HashMap<String, Double>() {{
        put("USD", 0.25);
        put("EUR", 0.25);
        put("JPY", 0.15);
        put("GBP", 0.10);
        put("CNY", 0.15);
        put("INR", 0.10);
    }};

    // Dynamic rates - will update in real-time
    Map<String, Double> rates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        edtAmount = findViewById(R.id.edtAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        txtResult = findViewById(R.id.txtResult);
        btnConvert = findViewById(R.id.btnConvert);

        String[] currencies = {"UniCurr", "USD", "EUR", "JPY", "GBP", "CNY", "INR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Initialize rates with mock values or fetch from API
        fetchRates();

        btnConvert.setOnClickListener(v -> convert());

        // Optional: convert instantly when amount or currency changes
        edtAmount.addTextChangedListener(new SimpleTextWatcher());
        spinnerFrom.setOnItemSelectedListener(new SimpleItemSelectedListener());
        spinnerTo.setOnItemSelectedListener(new SimpleItemSelectedListener());
    }

    private void fetchRates() {
        // TODO: Replace this with real API call to update rates map
        rates.put("USD", 1.0);
        rates.put("EUR", 1.1);
        rates.put("JPY", 0.007);
        rates.put("GBP", 1.3);
        rates.put("CNY", 0.14);
        rates.put("INR", 0.012);
    }

    private void convert() {
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        double amount;

        try {
            amount = Double.parseDouble(edtAmount.getText().toString());
        } catch (NumberFormatException e) {
            txtResult.setText("Invalid amount");
            return;
        }

        double result;

        if (from.equals("UniCurr")) {
            double unicurrValue = calculateUnicurrValue(); // in USD
            double toRate = rates.getOrDefault(to, 1.0);
            result = amount * unicurrValue / toRate;
        } else if (to.equals("UniCurr")) {
            double fromRate = rates.getOrDefault(from, 1.0);
            double unicurrValue = calculateUnicurrValue(); // in USD
            result = amount * fromRate / unicurrValue;
        } else {
            double fromRate = rates.getOrDefault(from, 1.0);
            double toRate = rates.getOrDefault(to, 1.0);
            result = amount * fromRate / toRate;
        }

        txtResult.setText(String.format("%.2f %s = %.2f %s", amount, from, result, to));
    }

    private double calculateUnicurrValue() {
        double sum = 0;
        for (String currency : pegWeights.keySet()) {
            double weight = pegWeights.get(currency);
            double rate = rates.getOrDefault(currency, 1.0);
            sum += weight * rate;
        }
        return sum; // 1 UniCurr value in USD
    }

    // To update conversion when input changes instantly:
    private class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            convert();
        }
    }

    private class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
            convert();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
