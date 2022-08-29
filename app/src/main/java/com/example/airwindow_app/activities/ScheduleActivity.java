package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.airwindow_app.api.WindowRepository;

import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    WindowRepository windowRepository;

    Integer timePickerHour, timePickerMinute;
    Button scheduleDailyTimePickerBT;
    ToggleButton scheduleDailyStateTB;

    EditText scheduleLaterMinuteETN;
    ToggleButton scheduleLaterStateTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        windowRepository = WindowRepository.getInstance();

        scheduleDailyTimePickerBT = findViewById(R.id.btScheduleDailyTimePicker);
        scheduleDailyStateTB = findViewById(R.id.tbScheduleDailyState);

        scheduleLaterMinuteETN = findViewById(R.id.etnScheduleLater);
        scheduleLaterStateTB = findViewById(R.id.tbScheduleLaterState);
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

        //TODO: For Each for each selected item
        //windowRepository.postScheduledTask(windowData, hour, minute, stateValue);
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

        //TODO: For Each for each selected item
        //windowRepository.postOneTimeTask(windowData, minute, stateValue);
        Toast.makeText(getApplicationContext(), getString(R.string.window_toast_onetimetask_text), Toast.LENGTH_LONG).show();


    }


}