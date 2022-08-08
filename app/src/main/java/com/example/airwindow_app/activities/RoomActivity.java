package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.WindowAdapter;
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.api.RoomRepository;
import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {

    RoomRepository roomRepository;

    RecyclerView recyclerView;

    TextView roomNameTV;
    TextView roomDescriptionTV;
    ImageView roomIconIV;

    Room roomData;
    int imageData;

    ArrayList<Window> windows;

    int windowImages[] = {R.drawable.window};

    // TODO: Actual images for rooms and not just placeholders
    int roomImages[] = {R.drawable.room_living, R.drawable.room_bath, R.drawable.room_office};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomRepository = RoomRepository.getInstance();

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

        if(getIntent().hasExtra("roomData")) {

            roomData = (Room) getIntent().getParcelableExtra("roomData");

        } else {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRoomData() {
        roomNameTV.setText(roomData.getName());
        roomDescriptionTV.setText(roomData.getDescription());
        roomIconIV.setImageResource(roomImages[roomData.getImage()]);
    }

    private void setWindowData() {
        Log.i("getWindowData", "Function triggered");

        // Clear arraylist to avoid duplicates
        windows.clear();

        Call<List<Window>> call = ApiClient.getInstance().getApiClient().getAllWindows(roomData.getId());

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

    public void editWindowDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etWindowName);
        editNameET.setText(roomData.getName());
        EditText editDescriptionET = editV.findViewById(R.id.etWindowDescription);
        editDescriptionET.setText(roomData.getDescription());

        builder.setMessage(R.string.edit_message)
                .setTitle(R.string.room_edit_title)
                .setView(editV)
                .setPositiveButton(R.string.window_edit_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Set text of EditText elements of the dialog
                        roomData.setName(editNameET.getText().toString());
                        roomData.setDescription(editDescriptionET.getText().toString());
                        // "refresh" data
                        setRoomData();

                        // Update (PUT) changed data on Backend
                        putRoom();

                        Log.i("Room", roomData.toString());
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.window_edit_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void putRoom() { roomRepository.putRoom(roomData); }
}