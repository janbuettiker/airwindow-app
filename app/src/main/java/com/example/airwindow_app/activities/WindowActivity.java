package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.airwindow_app.R;
import com.example.airwindow_app.api.ApiClient;
import com.example.airwindow_app.models.Window;
import com.google.gson.Gson;


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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WindowActivity extends AppCompatActivity {

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
                    putWindow(windowData);
                } else {
                    windowData.setWeatherAware("false");
                    putWindow(windowData);
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
        View editV = inflater.inflate(R.layout.window_edit_dialog, null);

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
                        putWindow(windowData);

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

    public void putWindow(Window w) {
        ApiClient.getInstance()
                .getApiClient()
                .putWindow(Long.valueOf(1), windowData.getId(), windowData)
                .enqueue(new Callback<Window>() {
                    @Override
                    public void onResponse(Call<Window> call, Response<Window> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse putWindow", "Successfully PUT window " + response.code() + response.message());
                        } else {
                            Log.e("onResponse putWindow", "Failed to PUT window " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Window> call, Throwable t) {
                        Log.e("onFailure putWindow", "Failed to PUT window " + t.getMessage());
                    }
                });
    }

    public void patchWindowState(Window w, String stateType, String stateValue) {
        ApiClient.getInstance()
                .getApiClient()
                .patchWindowState(w.getId(), stateType, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse patchWindowState", "Successfully updated state " + response.message());

                            if (stateValue.equals("OPEN")) {
                                windowOpenNowTB.setChecked(true);
                            } else {
                                windowOpenNowTB.setChecked(false);
                            }

                        } else {
                            Log.e("onResponse patchWindowState", "Failed to PATCH window state " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure patchWindowState", "Failed to PATCH window state " + t.getMessage());
                    }
                });
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

        ApiClient.getInstance()
                .getApiClient()
                .postScheduledTask(windowData.getId(), hour, minute, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse postScheduledTask", "Successfully scheduled task " + response.message());

                        } else {
                            Log.e("onResponse postScheduledTask", "Failed to POST scheduled task " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure postScheduledTask", "Failed to POST scheduled task " + t.getMessage());

                    }
                });
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

        ApiClient.getInstance()
                .getApiClient()
                .postOneTimeTask(windowData.getId(), minute, stateValue)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse postOneTimeTask", "Successfully scheduled one time task " + response.message());

                        } else {
                            Log.e("onResponse postOneTimeTask", "Failed to POST one time task " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure postOneTimeTask", "Failed to POST one time task " + t.getMessage());

                    }
                });
    }

    public void deleteWindow(View view) {

        // TODO: Room id programmatically
        Long roomId = Long.valueOf(1);

        ApiClient.getInstance()
                .getApiClient()
                .deleteWindow(roomId, windowData.getId())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Log.i("onResponse deleteWindow", "Successfully deleted window with id: " + windowData.getId());
                            finish();
                        } else {
                            Log.e("onResponse deleteWindow", "Failed to DELETE window");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("onFailure deleteWindow", "Failed to DELETE window " + t.getMessage());
                    }
                });
    }

    /*
        Not used but would work...
    */
    public void createWindow(View view) {
        Log.i("WindowActivity", "Create Window triggered");
        Window w = new Window();
        w.setName("Retrofit Window");
        w.setDescription("Created by Retrofit POST");
        w.setCurrentState("CLOSED");
        w.setDesiredState("CLOSED");
        ApiClient.getInstance().getApiClient().createWindow(w).enqueue(new Callback<Window>() {
            @Override
            public void onResponse(Call<Window> call, Response<Window> response) {
                Log.i("onResponse createWindow", "Successfully POSTed window " + response.code());
                Gson gson = new Gson();
                System.out.println(gson.toJson(w));
            }

            @Override
            public void onFailure(Call<Window> call, Throwable t) {
                Log.e("onFailure createWindow", "Failed to POST window: " + t.getMessage());

            }
        });
    }
}