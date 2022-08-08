package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.RoomAdapter;
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.api.RoomRepository;
import com.example.airwindow_app.models.Room;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RoomRepository roomRepository;

    RecyclerView recyclerView;

    TextView homeNameTV;
    TextView homeDescriptionTV;
    ImageView homeIconIV;
    String nameData, descriptionData;
    int imageData;

    // TODO: Dynamic home, but for the scope of this project,
    //  we will hardcode the one home that we have (id = 1)
    Long homeId;
    ArrayList<Room> rooms;

    String roomNames[], roomDescriptions[];
    int roomImages[] = {R.drawable.room_living, R.drawable.room_bath, R.drawable.room_office};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeId = Long.valueOf(1);
        roomRepository = RoomRepository.getInstance();

        rooms = new ArrayList<>();
        recyclerView = findViewById(R.id.rvHomeRoomList);


        roomNames = getResources().getStringArray(R.array.room_names);
        roomDescriptions = getResources().getStringArray(R.array.room_descriptions);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setRoomData();
    }


    private void setRoomData() {

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

                        RoomAdapter roomAdapter = new RoomAdapter(getApplicationContext(), rooms, roomImages);
                        recyclerView.setAdapter(roomAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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


    }
}