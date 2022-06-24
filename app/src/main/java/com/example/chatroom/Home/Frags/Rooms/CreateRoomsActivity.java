package com.example.chatroom.Home.Frags.Rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateRoomsActivity extends AppCompatActivity {
    Button createRoom;
    EditText title,maxUsers;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rooms);
        title = findViewById(R.id.createRoomTitle);
        maxUsers = findViewById(R.id.maxNumberOfUserRoom);
        createRoom = findViewById(R.id.createRoomButton);


        createRoom.setOnClickListener(click -> {
            if(title.getText().toString().equals("")){
                Toast.makeText(this, "Enter Some Title", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isDigitsOnly(maxUsers.getText().toString()) && !maxUsers.getText().toString().equals("0")){
                SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
                RoomClass roomClass = new RoomClass(maxUsers.getText().toString(),title.getText().toString(),sharedPreferences.getString("name",""),sharedPreferences.getString("email",""),auth.getUid());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms");
                UUID uuid = UUID.randomUUID();
                databaseReference.child(uuid.toString()).setValue(roomClass);
                databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child(uuid.toString()).child("RoomUsers");
                String time = System.currentTimeMillis() + "";
                databaseReference.child(time).child("authID").setValue(auth.getUid());
                databaseReference.child(time).child("name").setValue(sharedPreferences.getString("name",""));
                databaseReference.child(time).child("email").setValue(sharedPreferences.getString("email",""));
                Toast.makeText(this, "Room Created Successfully", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::finish,550);

            }
        });
    }
}