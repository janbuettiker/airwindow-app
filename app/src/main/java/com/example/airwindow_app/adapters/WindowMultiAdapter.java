package com.example.airwindow_app.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airwindow_app.R;
import com.example.airwindow_app.models.Window;

import java.util.ArrayList;
import java.util.List;

public class WindowMultiAdapter extends RecyclerView.Adapter<WindowMultiAdapter.MultiWindowViewHolder> {

    Context context;
    ArrayList<Window> windowData;

    public WindowMultiAdapter(Context ct, ArrayList<Window> windows) {
        this.context = ct;
        this.windowData = windows;
    }

    public void setWindowData(ArrayList<Window> windows) {
        this.windowData = windows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiWindowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.window_view_row, parent, false);
        return new MultiWindowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiWindowViewHolder holder, int position) {
        holder.bind(windowData.get(position));
    }

    @Override
    public int getItemCount() {
        return windowData.size();
    }

    public class MultiWindowViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, descriptionTV;
        ImageView iconIV;
        ConstraintLayout windowRowLayout;
        CardView windowRowCV;

        public MultiWindowViewHolder(@NonNull View windowItemView) {
            super(windowItemView);
            nameTV = windowItemView.findViewById(R.id.tvWindowOverviewName);
            descriptionTV = windowItemView.findViewById(R.id.tvWindowOverviewDescription);
            iconIV = windowItemView.findViewById(R.id.ivWindowOverviewIcon);
            windowRowLayout = windowItemView.findViewById(R.id.windowRowLayout);
            windowRowCV = windowItemView.findViewById(R.id.cvWindowOverview);
        }

        void bind(final Window window) {
            iconIV.setImageResource(R.drawable.window);
            nameTV.setText(window.getName());
            descriptionTV.setText(window.getDescription());

            windowRowLayout.setOnClickListener(view -> {
                window.setChecked(!window.isChecked());
                windowRowCV.setCardBackgroundColor(window.isChecked() ? Color.GRAY : Color.WHITE);
            });
        }

    }

    public ArrayList<Window> getAll() {
        return windowData;
    }

    public ArrayList<Window> getSelected() {
        ArrayList<Window> selected = new ArrayList<>();
        for (Window w : windowData) {
            if (w.isChecked()) {
                selected.add(w);
            }
        }
        return selected;
    }
}
