package com.example.airwindow_app.api;

import android.util.Log;

import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomRepository {

    private static RoomRepository instance = null;

    private ArrayList<Room> rooms;

    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    private RoomRepository() {
        rooms = new ArrayList<>();
    }

    public ArrayList<Room> getRoomsByHomeId(Long homeId) {

        rooms.clear();

        Call<List<Room>> call = ApiClient.getInstance().getApiClient().getAllRooms(homeId);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.code() == 200) {
                    Log.i("onResponse getRoomsByHomeId", "Successfully GOT rooms " + response.message());

                    List<Room> roomList = response.body();
                    for (int i = 0; i < roomList.size(); i++) {
                        rooms.add(roomList.get(i));
                    }
                } else {
                    Log.e("onResponse getRoomsByHomeId", "Failed to GET rooms " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e("onFailure getRoomsByHomeId", "Failed to GET rooms " + t.getMessage());
            }
        });

        return rooms;
    }
}
