package com.example.airwindow_app.api;

import com.example.airwindow_app.models.Window;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AirwindowApi {

    String BASE_URL = "https://airwindow-api.jblabs.ch/";
    /*
        So.. we cannot use the BASE_URL custom domain because I do not have
        a properly signed SSL certificate. OpenSSL didn't do the trick.
        So yes, to get access to our API with android, we need to use the backend url BASE_URL_BACKEND.
     */
    String BASE_URL_BACKEND = "https://airwindow-api-jdk.prouddune-5091e507.westeurope.azurecontainerapps.io/";
    String BASE_URL_DEV = "http://localhost:8080/";

    @GET("rooms/1/windows")
    Call<List<Window>> getAllWindows();

    @POST("rooms/1/windows")
    Call<Window> createWindow(@Body Window window);
}
