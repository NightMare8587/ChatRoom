package com.example.chatroom.Home.Frags.Rooms.JoinRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatroom.R;

public class JoinRoomActivity extends AppCompatActivity {
    String roomID,roomTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        roomID = getIntent().getStringExtra("roomID");
        roomTitle = getIntent().getStringExtra("roomTitle");
    }
}