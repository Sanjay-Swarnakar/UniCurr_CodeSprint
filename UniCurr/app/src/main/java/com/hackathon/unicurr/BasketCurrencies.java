package com.hackathon.unicurr;

import java.util.Map;

public class BasketCurrencies {

    // Define weights for the Peg Basket (Sum should be 1.0)
    public static final Map<String, Double> weights = Map.of(
            "USD", 0.4,
            "EUR", 0.3,
            "JPY", 0.15,
            "GBP", 0.15
    );

    // Compute weighted average = UniCurr value in USD
    public static double calculateUniCurr(Map<String, Double> rates) {
        double unicurrValue = 0.0;
        for (String currency : weights.keySet()) {
            if (rates.containsKey(currency)) {
                unicurrValue += weights.get(currency) * rates.get(currency);
            }
        }
        return unicurrValue;
    }
}