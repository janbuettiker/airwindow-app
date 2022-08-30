package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.WindowMultiAdapter;
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.api.RoomRepository;
import com.example.airwindow_app.api.WindowRepository;
import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity {

    RoomRepository roomRepository;
    WindowRepository windowRepository;

    Integer timePickerHour, timePickerMinute;
    Button scheduleDailyTimePickerBT;
    ToggleButton scheduleDailyStateTB;

    EditText scheduleLaterMinuteETN;
    ToggleButton scheduleLaterStateTB;

    RecyclerView recyclerView;
    WindowMultiAdapter adapter;
    ArrayList<Window> windows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        roomRepository = RoomRepository.getInstance();
        windowRepository = WindowRepository.getInstance();

        scheduleDailyTimePickerBT = findViewById(R.id.btScheduleDailyTimePicker);
        scheduleDailyStateTB = findViewById(R.id.tbScheduleDailyState);

        scheduleLaterMinuteETN = findViewById(R.id.etnScheduleLater);
        scheduleLaterStateTB = findViewById(R.id.tbScheduleLaterState);

        this.recyclerView = findViewById(R.id.rvWindowMultiSelect);
        adapter = new WindowMultiAdapter(this, windows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();

        setWindowData();
    }

    public void populateTimePicker(View view) {

        // Default time selection, also avoids another stupid nullpointer
        timePickerHour = 0;
        timePickerMinute = 0;

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                timePickerHour = hourOfDay;
                timePickerMinute = min;
                scheduleDailyTimePickerBT.setText(String.format(Locale.getDefault(), "%02d:%02d", timePickerHour, timePickerMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, timePickerHour, timePickerMinute, true);
        timePickerDialog.show();
    }

    public void postScheduledTask(View view) {

        Integer hour, minute;
        String stateValue;

        try {
            hour = Integer.valueOf(scheduleDailyTimePickerBT.getText().toString().split("\\:")[0]);
            minute = Integer.valueOf(scheduleDailyTimePickerBT.getText().toString().split("\\:")[1]);

        } catch (Exception e) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            Log.e("postScheduledTask", e.getMessage());
            return;
        }
        if (scheduleDailyStateTB.isChecked()) {
            stateValue = "OPEN";
        } else {
            stateValue = "CLOSED";
        }

        for (Window w: adapter.getSelected()) {
            windowRepository.postScheduledTask(w, hour, minute, stateValue);
        }
        Toast.makeText(getApplicationContext(), getString(R.string.window_toast_scheduledtask_text), Toast.LENGTH_LONG).show();

    }

    public void postOneTimeTask(View view) {

        Integer minute;
        String stateValue;

        try {
            minute = Integer.valueOf(scheduleLaterMinuteETN.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Please input minutes", Toast.LENGTH_SHORT).show();
            Log.e("postOneTimeTask", e.getMessage());
            return;
        }

        if (scheduleLaterStateTB.isChecked()) {
            stateValue = "OPEN";
        } else {
            stateValue = "CLOSED";
        }

        for (Window w: adapter.getSelected()) {
            windowRepository.postOneTimeTask(w, minute, stateValue);
        }
        Toast.makeText(getApplicationContext(), getString(R.string.window_toast_onetimetask_text), Toast.LENGTH_LONG).show();


    }

    private void setWindowData() {
        windows.clear();

        /*
            Protect your eyes!
            Spaghetti ahead, because my backend was built in a way, that you cannot just
            GET all windows, no matter the room, we need to do some nice italian pasta:
            - Get ALL Rooms from the home
            - For each Room
                - Get ALL Windows
                - And in the callback, update the recyclerview adapter with new data
            So you end up with encapsulated callbacks.. nice!
         */
        Call<List<Room>> roomCall = ApiClient.getInstance().getApiClient().getAllRooms(Long.valueOf(1));
        roomCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.i("setWindowData onResponse roomCall", "Triggered rooms length");
                List<Room> roomList = response.body();

                for (Room r : roomList) {
                    Call<List<Window>> windowCall = ApiClient.getInstance().getApiClient().getAllWindows(r.getId());
                    windowCall.enqueue(new Callback<List<Window>>() {
                        @Override
                        public void onResponse(Call<List<Window>> call, Response<List<Window>> response) {
                            Log.i("setWindowData onResponse windowCall", "Triggered");
                            windows.addAll(response.body());
                            adapter.setWindowData(windows);
                            Log.e("setWindowData onResponse windowCall", "windows length: " + windows.size());
                        }

                        @Override
                        public void onFailure(Call<List<Window>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });
    }


}