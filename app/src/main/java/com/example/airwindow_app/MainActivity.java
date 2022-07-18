package com.example.airwindow_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.airwindow_app.activities.HomeActivity;
import com.example.airwindow_app.activities.WindowOverviewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openWindowOverview(View view) {
        Intent intent = new Intent(this, WindowOverviewActivity.class);
        startActivity(intent);
    }

    public void openHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}