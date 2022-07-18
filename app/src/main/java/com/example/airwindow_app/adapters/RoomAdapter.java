package com.example.airwindow_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airwindow_app.R;
import com.example.airwindow_app.activities.RoomActivity;
import com.example.airwindow_app.activities.WindowOverviewActivity;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    String nameData[], descriptionData[];
    int imageData[];
    Context context;

    public RoomAdapter(Context ct, String names[], String descriptions[], int images[]) {
        context = ct;
        nameData = names;
        descriptionData = descriptions;
        imageData = images;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.room_view_row, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        // TODO Demo data to be replaced
        holder.nameTV.setText(nameData[position]);
        holder.descriptionTV.setText(descriptionData[position]);
        holder.iconIV.setImageResource(imageData[position]);

        holder.roomRowLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, RoomActivity.class);
            intent.putExtra("roomNameData", nameData[position]);
            intent.putExtra("roomDescriptionData", descriptionData[position]);
            intent.putExtra("roomImageData", imageData[position]);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return nameData.length;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, descriptionTV;
        ImageView iconIV;
        ConstraintLayout roomRowLayout;

        public RoomViewHolder(@NonNull View roomItemView) {
            super(roomItemView);
            nameTV = roomItemView.findViewById(R.id.tvRoomRowName);
            descriptionTV = roomItemView.findViewById(R.id.tvRoomRowDescription);
            iconIV = roomItemView.findViewById(R.id.ivRoomRowIcon);
            roomRowLayout = roomItemView.findViewById(R.id.roomRowLayout);
        }
    }
}
