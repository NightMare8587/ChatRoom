package com.example.chatroom.Home.Frags.Rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.Holder> {
    List<String> roomID = new ArrayList<>();
    List<String> roomTitle = new ArrayList<>();
    List<String> name = new ArrayList<>();
    List<String> email = new ArrayList<>();
    List<String> authID = new ArrayList<>();
    List<String> maxUsers = new ArrayList<>();
    List<String> totalUsers = new ArrayList<>();

    public RoomAdapter(List<String> roomID, List<String> roomTitle, List<String> name, List<String> email, List<String> authID,List<String> totalUsers,List<String> maxUsers) {
        this.roomID = roomID;
        this.roomTitle = roomTitle;
        this.totalUsers = totalUsers;
        this.name = name;
        this.email = email;
        this.authID = authID;
        this.maxUsers = maxUsers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.room_card_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(roomTitle.get(position));
        holder.users.setText(totalUsers.get(position) + "/" + maxUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return roomID.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        TextView title,users;
        Button join;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.roomTitleCardView);
            users = itemView.findViewById(R.id.totalRoomUsers);
            join = itemView.findViewById(R.id.joinRoomNowButton);
        }
    }
}
