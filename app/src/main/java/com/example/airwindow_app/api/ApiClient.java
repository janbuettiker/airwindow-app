package com.example.airwindow_app.api;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance = null;
    private AirwindowApi api;

    private ApiClient() {
        Log.i("ApiClient", "ApiClient Instance Created");
        api = createRetrofitClient();
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public AirwindowApi getApiClient() {
        return api;
    }

    private AirwindowApi createRetrofitClient() {
        Log.i("AirwindowApi", "Retrofit Client built");
        return new Retrofit.Builder()
                .baseUrl(AirwindowApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AirwindowApi.class);
    }
}
