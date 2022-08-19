package com.example.airwindow_app.api;

import android.os.StrictMode;
import android.util.Log;

import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

import java.io.IOException;
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

        /*
            Because we are doing stuff that we are not supposed to do (run api responses synchronously)
            we need to alter the ThreadPolicy. Else this synchronous call will be blocked by Android
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rooms = new ArrayList<>();
    }



    public ArrayList<Room> getRoomsByHomeId(Long homeId) {
        rooms.clear();

        Call<List<Room>> call = ApiClient.getInstance().getApiClient().getAllRooms(homeId);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (Integer.toString(response.code()).startsWith("2")) {
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

    /*
        Runs synchronous so we can make sure
        that when the room gets created, we do not run into
        a timing issue when re-populating the recycler view
     */
    public void createRoom(Long homeId, Room r) {


        try {
            ApiClient.getInstance()
                    .getApiClient()
                    .createRoom(homeId, r)
                    .execute();
            Log.i("createRoom execution", "Successfully POSTed room");
        } catch (IOException e) {
            Log.e("createRoom execution", "Failed to create room " + e.getMessage());
        }
    }

    public void putRoom(Long homeId, Room r) {
        ApiClient.getInstance()
                .getApiClient()
                .putRoom(homeId, r.getId(), r)
                .enqueue(new Callback<Room>() {
                    @Override
                    public void onResponse(Call<Room> call, Response<Room> response) {
                        if (Integer.toString(response.code()).startsWith("2")) {
                            Log.i("onResponse putRoom", "Successfully PUT room " + response.message());
                        } else {
                            Log.e("onResponse putRoom", "Failed to PUT room " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Room> call, Throwable t) {
                        Log.e("onFailure putRoom", "Failed to PUT room " + t.getMessage());
                    }
                });
    }

    public void deleteRoom(Long homeId, Room room) {
        try {
            ApiClient.getInstance()
                    .getApiClient()
                    .deleteRoom(homeId, room.getId())
                    .execute();

            Log.i("execute deleteRoom", "Successfully DELETEd room with id " + room.getId());

        } catch (IOException e) {
            Log.e("execute deleteRoom", "Failed to DELETE room " + e.getMessage());
        }
    }
}
