package com.example.airwindow_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.WindowAdapter;

public class RoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    TextView roomNameTV;
    TextView roomDescriptionTV;
    ImageView roomIconIV;
    String nameData, descriptionData;
    int imageData;


    String windowNames[], windowDescriptions[];
    int windowImages[] = {R.drawable.window};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomNameTV = findViewById(R.id.tvRoomName);
        roomDescriptionTV = findViewById(R.id.tvRoomDescription);
        roomIconIV = findViewById(R.id.ivRoomIcon);

        getData();
        setData();

        recyclerView = findViewById(R.id.rvRoomWindowList);

        windowNames = getResources().getStringArray(R.array.window_names);
        windowDescriptions = getResources().getStringArray(R.array.window_descriptions);

        WindowAdapter windowAdapter = new WindowAdapter(this, windowNames, windowDescriptions, windowImages);
        recyclerView.setAdapter(windowAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData() {
        if(getIntent().hasExtra("roomNameData") && getIntent().hasExtra("roomDescriptionData") && getIntent().hasExtra("roomImageData")) {

            nameData = getIntent().getStringExtra("roomNameData");
            descriptionData = getIntent().getStringExtra("roomDescriptionData");
            imageData = getIntent().getIntExtra("roomImageData", R.drawable.room_bed);
        } else {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        roomNameTV.setText(nameData);
        roomDescriptionTV.setText(descriptionData);
        roomIconIV.setImageResource(imageData);
    }
}