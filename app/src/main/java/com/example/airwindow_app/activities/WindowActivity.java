package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.airwindow_app.R;
import com.example.airwindow_app.api.WindowRepository;
import com.example.airwindow_app.models.Window;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class WindowActivity extends AppCompatActivity {

    WindowRepository windowRepository;

    TextView windowNameTV;
    TextView windowDescriptionTV;

    Window windowData;

    ToggleButton windowOpenNowTB;

    Button windowScheduleTimePickerBT;
    ToggleButton windowScheduleStateTB;
    Integer timePickerHour, timePickerMinute;

    EditText windowOneTimeMinuteETN;
    ToggleButton windowOneTimeStateTB;


    ToggleButton windowAutoModeTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        windowRepository = WindowRepository.getInstance();

        windowNameTV = findViewById(R.id.tvWindowName);
        windowDescriptionTV = findViewById(R.id.tvWindowDescription);

        windowOpenNowTB = findViewById(R.id.tbWindowOpenNow);
        windowOpenNowTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    patchWindowState(windowData,"DESIRED", "OPEN");
                } else {
                    patchWindowState(windowData, "DESIRED", "CLOSED");
                }
            }
        });

        windowScheduleTimePickerBT = findViewById(R.id.btWindowScheduleTimePicker);
        windowScheduleStateTB = findViewById(R.id.tbWindowScheduleState);

        windowOneTimeMinuteETN = findViewById(R.id.etnWindowOpenLater);
        windowOneTimeStateTB = findViewById(R.id.tbWindowOpenLaterState);

        windowAutoModeTB = findViewById(R.id.tbWindowAutoMode);
        windowAutoModeTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    windowData.setWeatherAware("true");
                    putWindow();
                } else {
                    windowData.setWeatherAware("false");
                    putWindow();
                }
            }
        });

        getData();
        setData();

    }

    private void getData() {
        if(getIntent().hasExtra("windowData")) {

            windowData = (Window) getIntent().getParcelableExtra("windowData");

        } else {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        windowNameTV.setText(windowData.getName());
        windowDescriptionTV.setText(windowData.getDescription());

        windowOpenNowTB.setChecked(windowData.getCurrentState().equals("OPEN"));
        windowAutoModeTB.setChecked(windowData.getWeatherAware().equals("true"));
    }

    public void populateTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                timePickerHour = hourOfDay;
                timePickerMinute = min;
                windowScheduleTimePickerBT.setText(String.format(Locale.getDefault(), "%02d:%02d", timePickerHour, timePickerMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, timePickerHour, timePickerMinute, true);
        timePickerDialog.show();
    }

    public void editWindowDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Create object of the dialog view to gain access to the EditText fields
        View editV = inflater.inflate(R.layout.edit_dialog, null);

        EditText editNameET = editV.findViewById(R.id.etWindowName);
        editNameET.setText(windowData.getName());
        EditText editDescriptionET = editV.findViewById(R.id.etWindowDescription);
        editDescriptionET.setText(windowData.getDescription());

        builder.setMessage(R.string.window_edit_message)
                .setTitle(R.string.window_edit_title)
                .setView(editV)
                .setPositiveButton(R.string.window_edit_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Set text of EditText elements of the dialog
                        windowData.setName(editNameET.getText().toString());
                        windowData.setDescription(editDescriptionET.getText().toString());
                        // "refresh" data
                        setData();

                        // Update (PUT) changed data on Backend
                        putWindow();

                        Log.i("Window", windowData.toString());
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

    public void putWindow() {
        windowRepository.putWindow(windowData);
    }

    public void patchWindowState(Window w, String stateType, String stateValue) {

        windowRepository.patchWindowState(w, stateType, stateValue);

        if (stateValue.equals("OPEN")) {
            windowOpenNowTB.setChecked(true);
        } else {
            windowOpenNowTB.setChecked(false);
        }
    }

    public void postScheduledTask(View view) {

        Integer hour, minute;
        String stateValue;

        try {
            hour = Integer.valueOf(windowScheduleTimePickerBT.getText().toString().split("\\:")[0]);
            minute = Integer.valueOf(windowScheduleTimePickerBT.getText().toString().split("\\:")[1]);

        } catch (Exception e) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            Log.e("postScheduledTask", e.getMessage());
            return;
        }
        if (windowScheduleStateTB.isChecked()) {
            stateValue = "OPEN";
        } else {
            stateValue = "CLOSED";
        }

        windowRepository.postScheduledTask(windowData, hour, minute, stateValue);
    }

    public void postOneTimeTask(View view) {

        Integer minute;
        String stateValue;

        try {
            minute = Integer.valueOf(windowOneTimeMinuteETN.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Please input minutes", Toast.LENGTH_SHORT).show();
            Log.e("postOneTimeTask", e.getMessage());
            return;
        }

        if (windowOneTimeStateTB.isChecked()) {
            stateValue = "OPEN";
        } else {
            stateValue = "CLOSED";
        }

        windowRepository.postOneTimeTask(windowData, minute, stateValue);

    }

    public void deleteWindow(View view) {

        windowRepository.deleteWindow(windowData);
        finish();
    }
}