package com.example.airwindow_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airwindow_app.R;
import com.example.airwindow_app.activities.WindowActivity;

import java.util.ArrayList;

public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.WindowViewHolder> {

    ArrayList<String> nameData;
    ArrayList<String> descriptionData;
    int imageData[];
    Context context;

    public WindowAdapter(Context ct, ArrayList<String> names, ArrayList<String> descriptions, int images[]) {
        context = ct;
        nameData = names;
        descriptionData = descriptions;
        imageData = images;

    }

    @NonNull
    @Override
    public WindowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.window_view_row, parent, false);
        return new WindowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WindowViewHolder holder, final int position) {
        // TODO Demo data, to be replaced
        // Sets the view items to positional values in the data arrays
        holder.nameTV.setText(nameData.get(position));
        holder.descriptionTV.setText(descriptionData.get(position));
        holder.iconIV.setImageResource(imageData[0]);

        holder.windowRowLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, WindowActivity.class);
            // Send data to Window Activity, omit imagedata as we do not care for the moment
            Log.i("OnclickListener", nameData.get(position));
            intent.putExtra("nameData", nameData.get(position));
            intent.putExtra("descriptionData", descriptionData.get(position));
            // Needed because we create the recyclerview outside of onCreate()
            // Thus, the Adapter is not properly attached
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return nameData.size();
    }

    public class WindowViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, descriptionTV;
        ImageView iconIV;
        ConstraintLayout windowRowLayout;

        public WindowViewHolder(@NonNull View windowItemView) {
            super(windowItemView);
            nameTV = windowItemView.findViewById(R.id.tvWindowOverviewName);
            descriptionTV = windowItemView.findViewById(R.id.tvWindowOverviewDescription);
            iconIV = windowItemView.findViewById(R.id.ivWindowOverviewIcon);
            windowRowLayout = windowItemView.findViewById(R.id.windowRowLayout);
        }
    }
}
