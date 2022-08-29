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
import com.example.airwindow_app.adapters.RoomAdapter;
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.api.RoomRepository;
import com.example.airwindow_app.models.Home;
import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

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

    // TODO: Dynamic home, but for the scope of this project,
    //  we will hardcode the one home that we have (id = 1)
    Long homeId;
    Home homeData;

    ArrayList<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(R.string.home_title);

        roomRepository = RoomRepository.getInstance();

        homeId = Long.valueOf(1);

        homeNameTV = findViewById(R.id.tvHomeName);
        homeDescriptionTV = findViewById(R.id.tvHomeDescription);

        rooms = new ArrayList<>();
        recyclerView = findViewById(R.id.rvHomeRoomList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getHomeData();
        setRoomData();

    }

    public void setHomeData() {
        homeNameTV.setText(homeData.getName());
        homeDescriptionTV.setText(homeData.getDescription());
    }

    public void getHomeData() {
        Call<Home> call = ApiClient.getInstance().getApiClient().getHomeById(homeId);
        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (Integer.toString(response.code()).startsWith("2")) {
                    Log.i("onResponse getHomeData", "Successfully GOT homes " + response.message());

                    homeData = response.body();
                    homeNameTV.setText(homeData.getName());
                    homeDescriptionTV.setText(homeData.getDescription());

                } else {
                    Log.e("onResponse getHomeData", "Failed to GET homes " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.e("onFailure getHomeData", "Failed to GET homes " + t.getMessage());
            }
        });
    }

    private void setRoomData() {

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

                        RoomAdapter roomAdapter = new RoomAdapter(getApplicationContext(), rooms);
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

    public void editHomeDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etName);
        editNameET.setText(homeData.getName());
        EditText editDescriptionET = editV.findViewById(R.id.etDescription);
        editDescriptionET.setText(homeData.getDescription());

        builder.setMessage(R.string.edit_message)
                .setTitle(R.string.home_edit_title)
                .setView(editV)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Set text of EditText elements of the dialog
                        homeData.setName(editNameET.getText().toString());
                        homeData.setDescription(editDescriptionET.getText().toString());

                        // Update (PUT) changed data on Backend
                        putHome();
                        Toast.makeText(getApplicationContext(), getString(R.string.home_toast_edit_text), Toast.LENGTH_LONG).show();

                        // "refresh" homedata
                        setHomeData();

                        Log.i("Home", homeData.toString());
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

    public void addRoomDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etName);
        editNameET.setHint(R.string.room_add_name_tv);
        EditText editDescriptionET = editV.findViewById(R.id.etDescription);
        editDescriptionET.setHint(R.string.room_add_description_tv);

        builder.setMessage(R.string.room_add_message)
                .setTitle(R.string.room_add_title)
                .setView(editV)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Room r = new Room();
                        r.setName(editNameET.getText().toString());
                        r.setDescription(editDescriptionET.getText().toString());

                        // create window
                        createRoom(r);

                        // refresh window recyclerview
                        setRoomData();

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

    public void putHome() {
        ApiClient.getInstance().getApiClient().putHome(homeId, homeData)
                .enqueue(new Callback<Home>() {
                    @Override
                    public void onResponse(Call<Home> call, Response<Home> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse putHome", "Successfully PUT home " + response.message());
                        } else {
                            Log.e("onResponse putHome", "Failed to PUT home " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Home> call, Throwable t) {
                        Log.e("onFailure putHome", "Failed to PUT home " + t.getMessage());
                    }
                });
    }

    public void createRoom(Room room) { roomRepository.createRoom(homeId, room); }
}