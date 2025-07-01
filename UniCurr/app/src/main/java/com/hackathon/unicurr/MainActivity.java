package com.hackathon.unicurr;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
    private List<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.lineChart);
        entries = new ArrayList<>();

        setupChart();
        simulateUniCurrTrend();
    }

    // Simulate 7-day value trend
    private void simulateUniCurrTrend() {
        for (int i = 0; i < 7; i++) {
            float value = (float) fetchUniCurrValue();
            entries.add(new Entry(i, value));
        }
        dataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    // Simulated fluctuating UniCurr value in USD
    private double fetchUniCurrValue() {
        double base = 1.1;
        double fluctuation = (Math.random() - 0.5) * 0.2; // ±0.1 variation
        return base + fluctuation;
    }

    private void setupChart() {
        dataSet = new LineDataSet(entries, "UniCurr Value (USD)");
        dataSet.setColor(Color.parseColor("#007BFF")); // Line color
        dataSet.setLineWidth(3f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Smooth curve

        // Fill under line for dramatic effect
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setFillColor(Color.parseColor("#99CCFF")); // Light fill

        lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // X Axis styling
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // Y Axis styling
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);

        // Chart styling
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.animateX(1200);
    }
}
