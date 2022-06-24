package com.example.chatroom.Home.Frags.Rooms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<String> roomID = new ArrayList<>();
    List<String> roomTitle = new ArrayList<>();
    List<String> name = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    List<String> email = new ArrayList<>();
    List<String> authID = new ArrayList<>();
    List<String> maxUsers = new ArrayList<>();
    List<String> roomTotalUsers = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rooms_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms");
        floatingActionButton = view.findViewById(R.id.createRoomFloatingActionButton);
        floatingActionButton.setOnClickListener(click -> {
            view.getContext().startActivity(new Intent(click.getContext(),CreateRoomsActivity.class));
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                       roomID.add(dataSnapshot.getKey());
                       roomTitle.add(dataSnapshot.child("title").getValue(String.class));
                       name.add(dataSnapshot.child("name").getValue(String.class));
                       email.add(dataSnapshot.child("email").getValue(String.class));
                       authID.add(dataSnapshot.child("authID").getValue(String.class));
                       maxUsers.add(dataSnapshot.child("maxUsers").getValue(String.class));
                       if(dataSnapshot.hasChild("RoomUsers"))
                           roomTotalUsers.add(dataSnapshot.child("RoomUsers").getChildrenCount() + "");
                       else
                           roomTotalUsers.add("0");
                   }
                   recyclerView.setAdapter(new RoomAdapter(roomID,roomTitle,name,email,authID,roomTotalUsers,maxUsers));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
                    roomID.clear();
                    roomTitle.clear();
                    name.clear();
                    email.clear();
                    authID.clear();
                    maxUsers.clear();
                    roomTotalUsers.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        roomID.add(dataSnapshot.getKey());
                        roomTitle.add(dataSnapshot.child("title").getValue(String.class));
                        name.add(dataSnapshot.child("name").getValue(String.class));
                        email.add(dataSnapshot.child("email").getValue(String.class));
                        authID.add(dataSnapshot.child("authID").getValue(String.class));
                        maxUsers.add(dataSnapshot.child("maxUsers").getValue(String.class));
                        if(dataSnapshot.hasChild("RoomUsers"))
                            roomTotalUsers.add(dataSnapshot.child("RoomUsers").getChildrenCount() + "");
                        else
                            roomTotalUsers.add("0");
                    }
                    recyclerView.setAdapter(new RoomAdapter(roomID,roomTitle,name,email,authID,roomTotalUsers,maxUsers));
                }else{
                    roomID.clear();
                    recyclerView.setAdapter(new RoomAdapter(roomID,roomTitle,name,email,authID,roomTotalUsers,maxUsers));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
