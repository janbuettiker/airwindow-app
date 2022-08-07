package com.example.airwindow_app.api;

import android.util.Log;

import com.example.airwindow_app.models.Window;

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

    public void createWindow(Window w) {
        ApiClient.getInstance().getApiClient().createWindow(w).enqueue(new Callback<Window>() {
            @Override
            public void onResponse(Call<Window> call, Response<Window> response) {
                if (response.code() == 200) {
                    Log.i("onResponse createWindow", "Successfully POSTed window " + response.message());
                } else {
                    Log.e("onResponse createWindow", "Failed to POST window " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Window> call, Throwable t) {
                Log.e("onFailure createWindow", "Failed to POST window: " + t.getMessage());

            }
        });
    }

    public void putWindow(Window w) {
        ApiClient.getInstance()
                .getApiClient()
                .putWindow(Long.valueOf(1), w.getId(), w)
                .enqueue(new Callback<Window>() {
                    @Override
                    public void onResponse(Call<Window> call, Response<Window> response) {
                        if (response.code() == 200) {
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

    public void deleteWindow(Window w) {

        // TODO: Room id programmatically
        Long roomId = Long.valueOf(1);

        ApiClient.getInstance()
                .getApiClient()
                .deleteWindow(roomId, w.getId())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse deleteWindow", "Successfully deleted window with id: " + w.getId());
                        } else {
                            Log.e("onResponse deleteWindow", "Failed to DELETE window");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure deleteWindow", "Failed to DELETE window " + t.getMessage());
                    }
                });
    }

    public void patchWindowState(Window w, String stateType, String stateValue) {
        ApiClient.getInstance()
                .getApiClient()
                .patchWindowState(w.getId(), stateType, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
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
                        if (response.code() == 200) {
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
                        if (response.code() == 200) {
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
