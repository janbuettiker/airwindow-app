package com.example.airwindow_app.api;

import android.os.StrictMode;
import android.util.Log;

import com.example.airwindow_app.models.Window;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WindowRepository {

    private static WindowRepository instance = null;

    public static synchronized WindowRepository getInstance() {
        if (instance == null) {
            instance = new WindowRepository();
        }
        return instance;
    }

    public WindowRepository() {

        /*
            Because we are doing stuff that we are not supposed to do (run api responses synchronously)
            we need to alter the ThreadPolicy. Else this synchronous call will be blocked by Android
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /*
        Runs synchronous so we can make sure
        that when the window gets created, we do not run into
        a timing issue when re-populating the recycler view
    */
    public void createWindow(Long roomId, Window w) {
        try {
            ApiClient.getInstance()
                    .getApiClient()
                    .createWindow(roomId, w)
                    .execute();
            Log.i("execute createWindow", "Successfully POSTed window");

        }  catch (IOException e) {
            Log.e("execute createWindow", "Failed to POST window " + e.getMessage());
        }
    }

    public void putWindow(Long roomId, Window w) {
        ApiClient.getInstance()
                .getApiClient()
                .putWindow(roomId, w.getId(), w)
                .enqueue(new Callback<Window>() {
                    @Override
                    public void onResponse(Call<Window> call, Response<Window> response) {
                        if (Integer.toString(response.code()).startsWith("2")) {
                            Log.i("onResponse putWindow", "Successfully PUT window " + response.message());
                        } else {
                            Log.e("onResponse putWindow", "Failed to PUT window " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Window> call, Throwable t) {
                        Log.e("onFailure putWindow", "Failed to PUT window " + t.getMessage());
                    }
                });
    }

    public void deleteWindow(Long roomId, Window w) {
        try {
            ApiClient.getInstance()
                    .getApiClient()
                    .deleteWindow(roomId, w.getId())
                    .execute();
            Log.i("execute deleteWindow", "Successfully DELETEd window with id " + w.getId());
        } catch (IOException e) {
            Log.e("execute deleteWindow", "Failed to DELETE window");
        }

    }

    public void patchWindowState(Window w, String stateType, String stateValue) {
        ApiClient.getInstance()
                .getApiClient()
                .patchWindowState(w.getId(), stateType, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (Integer.toString(response.code()).startsWith("2")) {
                            Log.i("onResponse patchWindowState", "Successfully updated state " + response.message());
                        } else {
                            Log.e("onResponse patchWindowState", "Failed to PATCH window state " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure patchWindowState", "Failed to PATCH window state " + t.getMessage());
                    }
                });
    }

    public void postScheduledTask(Window w, Integer hour, Integer minute, String stateValue) {
        ApiClient.getInstance()
                .getApiClient()
                .postScheduledTask(w.getId(), hour, minute, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (Integer.toString(response.code()).startsWith("2")) {
                            Log.i("onResponse postScheduledTask", "Successfully scheduled task " + response.message());

                        } else {
                            Log.e("onResponse postScheduledTask", "Failed to POST scheduled task " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure postScheduledTask", "Failed to POST scheduled task " + t.getMessage());

                    }
                });
    }

    public void postOneTimeTask(Window w, Integer minute, String stateValue) {
        ApiClient.getInstance()
                .getApiClient()
                .postOneTimeTask(w.getId(), minute, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (Integer.toString(response.code()).startsWith("2")) {
                            Log.i("onResponse postOneTimeTask", "Successfully scheduled one time task " + response.message());

                        } else {
                            Log.e("onResponse postOneTimeTask", "Failed to POST one time task " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure postOneTimeTask", "Failed to POST one time task " + t.getMessage());
                        }
                    });
    }

}
