package com.example.chatroom.Home.Frags.Rooms.JoinRoom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinRoomActivity extends AppCompatActivity {
    String roomID,roomTitle,joinTime;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    EditText editText;
    List<String> time = new ArrayList<>();
    List<String> message = new ArrayList<>();
    List<String> senderID = new ArrayList<>();
    List<String> name = new ArrayList<>();
    Button send;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        roomID = getIntent().getStringExtra("roomID");
        roomTitle = getIntent().getStringExtra("roomTitle");
        joinTime = getIntent().getStringExtra("joinTime");
        recyclerView = findViewById(R.id.joinRoomActivityRecyclerView);
        editText = findViewById(R.id.editTextSendMessageJoinRoom);
        send = findViewById(R.id.sendMessageButtonRooms);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child(roomID).child("RoomChats");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        time.add(dataSnapshot.getKey());
                        message.add(dataSnapshot.child("message").getValue(String.class));
                        name.add(dataSnapshot.child("name").getValue(String.class));
                        senderID.add(dataSnapshot.child("senderID").getValue(String.class));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(JoinRoomActivity.this));
                    recyclerView.setAdapter(new RoomAdapter(time,message,senderID,name));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(click -> {
            if(editText.getText().toString().equals("")){
                Toast.makeText(this, "Enter Something :)", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child(roomID).child("RoomChats");
            String time = System.currentTimeMillis() + "";
            databaseReference.child(time).child("message").setValue(editText.getText().toString());
            databaseReference.child(time).child("senderID").setValue(auth.getUid());
            SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
            databaseReference.child(time).child("name").setValue(sharedPreferences.getString("name",""));
            editText.setText("");
        });
    databaseReference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            updateChild();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            updateChild();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            updateChild();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    private void updateChild() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    message.clear();
                    time.clear();
                    senderID.clear();
                    name.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        time.add(dataSnapshot.getKey());
                        message.add(dataSnapshot.child("message").getValue(String.class));
                        name.add(dataSnapshot.child("name").getValue(String.class));
                        senderID.add(dataSnapshot.child("senderID").getValue(String.class));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(JoinRoomActivity.this));
                    recyclerView.setAdapter(new RoomAdapter(time,message,senderID,name));
                }else{
                    message.clear();
                    time.clear();
                    recyclerView.setAdapter(new RoomAdapter(time,message,senderID,name));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}