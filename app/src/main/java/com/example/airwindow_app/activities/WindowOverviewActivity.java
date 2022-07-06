package com.example.airwindow_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.airwindow_app.R;
import com.example.airwindow_app.adapters.WindowAdapter;

public class WindowOverviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String windowNames[], windowDescriptions[];
    int windowImages[] = {R.drawable.window_closed, R.drawable.window_opened};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_overview);

        recyclerView = findViewById(R.id.windowRecyclerView);

        windowNames = getResources().getStringArray(R.array.window_names);
        windowDescriptions = getResources().getStringArray(R.array.window_descriptions);

        WindowAdapter windowAdapter = new WindowAdapter(this, windowNames, windowDescriptions, windowImages);
        recyclerView.setAdapter(windowAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}