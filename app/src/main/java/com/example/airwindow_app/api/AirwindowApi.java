package com.example.airwindow_app.api;

import com.example.airwindow_app.models.Window;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AirwindowApi {

    String BASE_URL = "https://airwindow-api.jblabs.ch/";
    String BASE_URL_DEV = "http://localhost:8080/";

    @GET("rooms/1/windows")
    Call<List<Window>> getAllWindows();

    @GET("windows/1/state?state=CURRENT")
    Call<String> getCurrentState();
}
