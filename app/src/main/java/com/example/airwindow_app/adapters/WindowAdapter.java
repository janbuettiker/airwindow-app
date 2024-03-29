package com.example.airwindow_app.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;

public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.WindowViewHolder> {

    Long roomId;
    ArrayList<Window> windowData;
    int imageData[];
    Context context;

    public WindowAdapter(Context ct, Long roomId, ArrayList<Window> windows, int images[]) {
        this.roomId = roomId;
        context = ct;
        windowData = windows;
        imageData = images;
    }

    public void setWindowData(ArrayList<Window> windows) {
        this.windowData = windows;
        notifyDataSetChanged();
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
            holder.bind(windowData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return windowData.size();
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

        void bind(final Window window, final int position) {
            nameTV.setText(window.getName());
            descriptionTV.setText(window.getDescription());
            iconIV.setImageResource(imageData[0]);

            windowRowLayout.setOnClickListener(view -> {
                Intent intent = new Intent(context, WindowActivity.class);

                // Send data to Window Activity, omit imagedata as we do not care for the moment
                intent.putExtra("windowData", window);
                intent.putExtra("roomId", roomId);

                // Needed because we create the recyclerview outside of onCreate()
                // Thus, the Adapter is not properly attached
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
    }
}
