package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.RoomAdapter;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String roomNames[], roomDescriptions[];
    int roomImages[] = {R.drawable.dining_room, R.drawable.bed_room, R.drawable.office_room};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.rvHomeRoomList);

        roomNames = getResources().getStringArray(R.array.room_names);
        roomDescriptions = getResources().getStringArray(R.array.room_descriptions);

        RoomAdapter roomAdapter = new RoomAdapter(this, roomNames, roomDescriptions, roomImages);
        recyclerView.setAdapter(roomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}