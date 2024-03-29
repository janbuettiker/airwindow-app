package com.example.airwindow_app.api;

import android.util.Log;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
        return new Retrofit.Builder()
                .baseUrl(AirwindowApi.BASE_URL_BACKEND)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build()
                .create(AirwindowApi.class);
    }
}
