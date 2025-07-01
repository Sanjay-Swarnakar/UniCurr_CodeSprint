package com.hackathon.unicurr;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrencyAPI {

    public interface Callback {
        void onSuccess(Map<String, Double> rates);
        void onError(Exception e);
    }

    public static void fetchRates(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                JsonObject json = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonObject rateObj = json.getAsJsonObject("rates");

                Map<String, Double> rates = new HashMap<>();
                for (String key : BasketCurrencies.weights.keySet()) {
                    rates.put(key, rateObj.get(key).getAsDouble());
                }
                callback.onSuccess(rates);
            }
        });
    }
}
