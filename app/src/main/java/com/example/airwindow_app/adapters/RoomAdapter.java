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
import com.example.airwindow_app.activities.RoomActivity;
import com.example.airwindow_app.models.Room;

import java.util.ArrayList;
import java.util.Random;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    ArrayList<Room> roomData;
    int imageData[];
    Context context;

    public RoomAdapter(Context ct, ArrayList<Room> rooms, int images[]) {
        context = ct;
        roomData = rooms;
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

        // random number between 0 - 2 for room Images
        final int random = new Random().nextInt(3);

        holder.nameTV.setText(roomData.get(position).getName());
        holder.descriptionTV.setText(roomData.get(position).getDescription());
        holder.iconIV.setImageResource(imageData[random]);

        holder.roomRowLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, RoomActivity.class);

            intent.putExtra("roomDaata", roomData.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomData.size();
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
