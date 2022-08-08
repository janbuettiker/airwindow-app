package com.example.airwindow_app.activities;

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
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    TextView roomNameTV;
    TextView roomDescriptionTV;
    ImageView roomIconIV;
    String nameData, descriptionData;
    int imageData;

    ArrayList<Window> windows;

    int windowImages[] = {R.drawable.window};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomNameTV = findViewById(R.id.tvRoomName);
        roomDescriptionTV = findViewById(R.id.tvRoomDescription);
        roomIconIV = findViewById(R.id.ivRoomIcon);

        windows = new ArrayList<>();
        getRoomDataFromIntent();
        setRoomData();
        recyclerView = findViewById(R.id.rvRoomWindowList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setWindowData();
    }

    private void getRoomDataFromIntent() {
        if(getIntent().hasExtra("roomNameData") && getIntent().hasExtra("roomDescriptionData") && getIntent().hasExtra("roomImageData")) {

            nameData = getIntent().getStringExtra("roomNameData");
            descriptionData = getIntent().getStringExtra("roomDescriptionData");
            imageData = getIntent().getIntExtra("roomImageData", R.drawable.room_bed);
        } else {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRoomData() {
        roomNameTV.setText(nameData);
        roomDescriptionTV.setText(descriptionData);
        roomIconIV.setImageResource(imageData);
    }

    private void setWindowData() {
        Log.i("getWindowData", "Function triggered");

        // Clear arraylist to avoid duplicates
        windows.clear();

        Call<List<Window>> call = ApiClient.getInstance().getApiClient().getAllWindows();

        call.enqueue(new Callback<List<Window>>() {
            @Override
            public void onResponse(Call<List<Window>> call, Response<List<Window>> response) {
                Log.i("onResponse", "Code: " + response.code() + response.body());

                List<Window> windowList = response.body();
                Log.i("windowList", "Size: " + windowList.size());

                for (int i = 0; i < windowList.size(); i++) {
                    windows.add(windowList.get(i));

                    WindowAdapter windowAdapter = new WindowAdapter(getApplicationContext(), windows, windowImages);
                    recyclerView.setAdapter(windowAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<List<Window>> call, Throwable t) {
                Log.i("onFailure getWindowData", t.getMessage());
                Toast.makeText(getApplicationContext(), "Could not GET data from API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}