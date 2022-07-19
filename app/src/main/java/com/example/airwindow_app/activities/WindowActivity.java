package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.airwindow_app.R;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class WindowActivity extends AppCompatActivity {

    TextView windowNameTV;
    TextView windowDescriptionTV;
    String nameData, descriptionData;

    Button windowTimePickerBT;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        windowNameTV = findViewById(R.id.tvWindowName);
        windowDescriptionTV = findViewById(R.id.tvWindowDescription);

        windowTimePickerBT = findViewById(R.id.btWindowTimePicker);

        getData();
        setData();

    }

    private void getData() {
        if(getIntent().hasExtra("nameData") && getIntent().hasExtra("descriptionData")) {

            nameData = getIntent().getStringExtra("nameData");
            descriptionData = getIntent().getStringExtra("descriptionData");

        } else {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        windowNameTV.setText(nameData);
        windowDescriptionTV.setText(descriptionData);
    }

    public void populateTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                hour = hourOfDay;
                minute = min;
                windowTimePickerBT.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.show();
    }

    public void editWindowDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        builder.setMessage(R.string.window_edit_message)
                .setTitle(R.string.window_edit_title)
                .setView(inflater.inflate(R.layout.window_edit_dialog, null))
                .setPositiveButton(R.string.window_edit_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Execute PATCH Request on backend
                        Log.i("Window OK Button", "OK Button pressed");
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
}