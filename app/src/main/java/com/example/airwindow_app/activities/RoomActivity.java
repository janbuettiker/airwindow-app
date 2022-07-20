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
import com.example.airwindow_app.api.AirwindowApi;
import com.example.airwindow_app.models.Window;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        getRoomDataFromIntent();
        setRoomData();

        getWindowData();
        getWindowCurrentState();

        recyclerView = findViewById(R.id.rvRoomWindowList);

        windowNames = getResources().getStringArray(R.array.window_names);
        windowDescriptions = getResources().getStringArray(R.array.window_descriptions);


        WindowAdapter windowAdapter = new WindowAdapter(this, windowNames, windowDescriptions, windowImages);
        recyclerView.setAdapter(windowAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void getWindowData() {
        Log.i("getWindowData", "Function triggered");
        Call<List<Window>> call = getApiClient().getAllWindows();

        call.enqueue(new Callback<List<Window>>() {
            @Override
            public void onResponse(Call<List<Window>> call, Response<List<Window>> response) {
                Log.i("onResponse", "Code: " + response.code());

                List<Window> windowList = response.body();

                for (int i = 0; i < windowList.size(); i++) {
                    windowNames[i] = windowList.get(i).getName();
                    windowDescriptions[i] = windowList.get(i).getDescription();

                    Log.i("windowName", windowList.get(i).getName());
                    Log.i("windowDescription", windowList.get(i).getDescription());
                    Log.i("windowCurrentState", windowList.get(i).getCurrentState());
                }
            }

            @Override
            public void onFailure(Call<List<Window>> call, Throwable t) {
                Log.i("onFailure getWindowData", t.getMessage());
                Toast.makeText(getApplicationContext(), "Could not GET data from API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWindowCurrentState() {
        Call<String> call = getApiClient().getCurrentState();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("windowState", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("onFailure getWindowCurrentState", t.getMessage());
                Toast.makeText(getApplicationContext(), "Could not GET data from API", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static AirwindowApi getApiClient() {

        return new Retrofit.Builder()
                .baseUrl(AirwindowApi.BASE_URL_BACKEND)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AirwindowApi.class);
    }
}