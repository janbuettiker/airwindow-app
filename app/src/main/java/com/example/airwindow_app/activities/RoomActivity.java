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
import com.example.airwindow_app.api.WindowRepository;
import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {

    RoomRepository roomRepository;
    WindowRepository windowRepository;

    RecyclerView recyclerView;
    WindowAdapter windowAdapter;

    TextView roomNameTV;
    TextView roomDescriptionTV;
    ImageView roomIconIV;

    // TODO: Dynamic home, but for the scope of this project,
    //  we will hardcode the one home that we have (id = 1)
    Long homeId;
    Room roomData;

    ArrayList<Window> windows;

    int windowImages[] = {R.drawable.window};

    // TODO: Actual images for rooms and not just placeholders
    int roomImages[] = {R.drawable.room_living, R.drawable.room_bath, R.drawable.room_office};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomRepository = RoomRepository.getInstance();
        windowRepository = WindowRepository.getInstance();

        homeId = Long.valueOf(1);

        roomNameTV = findViewById(R.id.tvRoomName);
        roomDescriptionTV = findViewById(R.id.tvRoomDescription);
        roomIconIV = findViewById(R.id.ivRoomIcon);

        windows = new ArrayList<>();
        getRoomDataFromIntent();

    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id.rvRoomWindowList);

        setRoomData();
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

        // Clear arraylist to avoid duplicates
        windows.clear();

        Call<List<Window>> call = ApiClient.getInstance().getApiClient().getAllWindows(roomData.getId());
        call.enqueue(new Callback<List<Window>>() {
            @Override
            public void onResponse(Call<List<Window>> call, Response<List<Window>> response) {
                Log.i("onResponse setWindowData", "Code: " + response.code());

                List<Window> windowList = response.body();

                for (int i = 0; i < windowList.size(); i++) {
                    windows.add(windowList.get(i));

                    WindowAdapter windowAdapter = new WindowAdapter(getApplicationContext(), roomData.getId(), windows, windowImages);
                    recyclerView.setAdapter(windowAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    windowAdapter.notifyItemRangeChanged(0, windowAdapter.getItemCount());
                }
            }

            @Override
            public void onFailure(Call<List<Window>> call, Throwable t) {
                Log.i("onFailure getWindowData", t.getMessage());
                Toast.makeText(getApplicationContext(), "Could not GET data from API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editRoomDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etName);
        editNameET.setText(roomData.getName());
        EditText editDescriptionET = editV.findViewById(R.id.etDescription);
        editDescriptionET.setText(roomData.getDescription());

        builder.setMessage(R.string.edit_message)
                .setTitle(R.string.room_edit_title)
                .setView(editV)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Set text of EditText elements of the dialog
                        roomData.setName(editNameET.getText().toString());
                        roomData.setDescription(editDescriptionET.getText().toString());

                        // Update (PUT) changed data on Backend
                        putRoom();
                        Toast.makeText(getApplicationContext(), getString(R.string.room_toast_edit_text), Toast.LENGTH_LONG).show();


                        // "refresh" data
                        setRoomData();

                        Log.i("Room", roomData.toString());
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addWindowDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etName);
        editNameET.setHint(R.string.window_add_name_tv);
        EditText editDescriptionET = editV.findViewById(R.id.etDescription);
        editDescriptionET.setHint(R.string.window_add_description_tv);

        builder.setMessage(R.string.window_add_message)
                .setTitle(R.string.window_add_title)
                .setView(editV)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Window w = new Window();
                        w.setCurrentState("CLOSED");
                        w.setDesiredState("CLOSED");
                        w.setWeatherAware("false");
                        w.setName(editNameET.getText().toString());
                        w.setDescription(editDescriptionET.getText().toString());

                        // create window
                        createWindow(w);

                        // refresh window recyclerview
                        setWindowData();

                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteRoomDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.room_delete_title)
                .setMessage(R.string.room_delete_message)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRoom();
                        setRoomData();
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void putRoom() { roomRepository.putRoom(homeId, roomData); }
    public void deleteRoom() { roomRepository.deleteRoom(homeId, roomData); }

    public void createWindow(Window w) { windowRepository.createWindow(roomData.getId(), w); }
}