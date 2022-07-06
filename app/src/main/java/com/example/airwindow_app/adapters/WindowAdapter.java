package com.example.airwindow_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airwindow_app.R;

public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.WindowViewHolder> {

    String nameData[], descriptionData[];
    int imageData[];
    Context context;

    public WindowAdapter(Context ct, String names[], String descriptions[], int images[]) {
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
    public void onBindViewHolder(@NonNull WindowViewHolder holder, int position) {
        // Demo data, to be replaced
        // Sets the view items to positional values in the data arrays
        holder.nameTV.setText(nameData[position]);
        holder.descriptionTV.setText(descriptionData[position]);
        holder.iconIV.setImageResource(imageData[0]);
    }

    @Override
    public int getItemCount() {
        return nameData.length;
    }

    public class WindowViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, descriptionTV;
        ImageView iconIV;

        public WindowViewHolder(@NonNull View windowItemView) {
            super(windowItemView);
            nameTV = windowItemView.findViewById(R.id.tvWindowName);
            descriptionTV = windowItemView.findViewById(R.id.tvWindowDescription);
            iconIV = windowItemView.findViewById(R.id.ivWindowIcon);
        }
    }
}
