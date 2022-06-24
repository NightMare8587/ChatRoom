package com.example.chatroom.Home.Frags.Rooms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.Home.Frags.Rooms.JoinRoom.JoinRoomActivity;
import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(roomTitle.get(position));
        holder.users.setText(totalUsers.get(position) + "/" + maxUsers.get(position));

        holder.join.setOnClickListener(click -> {
            if(Integer.parseInt(totalUsers.get(position)) == Integer.parseInt(maxUsers.get(position))){
                Toast.makeText(click.getContext(), "Room is Full Right Now", Toast.LENGTH_SHORT).show();
            }else{
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child(roomID.get(position)).child("RoomUsers");
                String time = System.currentTimeMillis() + "";
                FirebaseAuth auth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = click.getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                databaseReference.child(time).child("authID").setValue(auth.getUid());
                databaseReference.child(time).child("name").setValue(sharedPreferences.getString("name",""));
                databaseReference.child(time).child("email").setValue(sharedPreferences.getString("email",""));
                Intent intent = new Intent(click.getContext(), JoinRoomActivity.class);
                intent.putExtra("roomID",roomID.get(position));
                intent.putExtra("roomTitle",roomTitle.get(position));
                intent.putExtra("name",name.get(position));
                intent.putExtra("email",email.get(position));
                intent.putExtra("authID",authID.get(position));
                click.getContext().startActivity(intent);
            }
        });
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
