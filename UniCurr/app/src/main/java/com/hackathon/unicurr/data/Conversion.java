package com.hackathon.unicurr.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversion_table")
public class Conversion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double result;
    private long timestamp;

    // Constructor
    public Conversion(String fromCurrency, String toCurrency, double amount, double result, long timestamp) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.result = result;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public double getAmount() { return amount; }
    public double getResult() { return result; }
    public long getTimestamp() { return timestamp; }
}