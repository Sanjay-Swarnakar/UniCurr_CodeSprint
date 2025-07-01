package com.hackathon.unicurr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnBalance, btnConvert, btnSend, btnProfile;
    LineChart lineChart;
    List<Entry> entries = new ArrayList<>();
    LineDataSet dataSet;
    LineData lineData;
    Handler handler = new Handler();
    int timeIndex = 0;  // x axis: time increments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnBalance = findViewById(R.id.btnBalance);
        btnConvert = findViewById(R.id.btnConvert);
        btnSend = findViewById(R.id.btnSend);
        btnProfile = findViewById(R.id.btnProfile);

        // Initialize chart
        lineChart = findViewById(R.id.lineChart);
        setupChart();

        // Start updating chart values every 5 seconds
        startUpdatingUniCurrValue();

        // Set button click listeners
        btnBalance.setOnClickListener(v -> startActivity(new Intent(this, BalanceActivity.class)));
        btnConvert.setOnClickListener(v -> startActivity(new Intent(this, ConvertActivity.class)));
        btnSend.setOnClickListener(v -> startActivity(new Intent(this, SendMoneyActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }

    private void setupChart() {
        dataSet = new LineDataSet(entries, "UniCurr Value Over Time");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.invalidate();
    }

    private void startUpdatingUniCurrValue() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double newUniCurrValue = fetchUniCurrValue();
                addEntry((float) newUniCurrValue);
                handler.postDelayed(this, 5000); // repeat every 5 seconds
            }
        }, 0);
    }

    // Mock method to get UniCurr weighted average of currencies
    private double fetchUniCurrValue() {
        // TODO: Replace with real API call and weighted average calculation

        double usd = 1.0;  // example USD rate
        double eur = 0.9;
        double jpy = 110.0;

        double wUsd = 0.5;
        double wEur = 0.3;
        double wJpy = 0.2;

        // Weighted average (divide JPY for scale)
        return usd * wUsd + eur * wEur + (jpy / 100) * wJpy;
    }

    private void addEntry(float value) {
        entries.add(new Entry(timeIndex++, value));
        dataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.setVisibleXRangeMaximum(20);  // show last 20 points
        lineChart.moveViewToX(lineData.getEntryCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // prevent leaks
    }
}
