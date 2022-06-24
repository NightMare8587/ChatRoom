package com.example.chatroom.Home.Frags.Posts;

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

import com.example.chatroom.Home.CreatePost.CreateNewPostActivity;
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

public class PostsFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> postID = new ArrayList<>();
    List<String> creatorID = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> email = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    RecyclerView recyclerView;
    List<String> name = new ArrayList<>();
    List<String> upvotes = new ArrayList<>();
    DatabaseReference reference;
    List<String> downvotes = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.posts_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.createPostFloatingActionButton);
        floatingActionButton.setOnClickListener(click -> {
            startActivity(new Intent(click.getContext(), CreateNewPostActivity.class));
        });
        recyclerView = view.findViewById(R.id.cyclerViewPostCreation);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");
        reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        postID.add(dataSnapshot.getKey());
                        title.add(dataSnapshot.child("title").getValue(String.class));
                        email.add(dataSnapshot.child("userEmail").getValue(String.class));
                        name.add(dataSnapshot.child("userName").getValue(String.class));
                        upvotes.add(dataSnapshot.child("upVotes").getValue(String.class));
                        downvotes.add(dataSnapshot.child("downVotes").getValue(String.class));
                        if(dataSnapshot.hasChild("comments"))
                            comments.add(dataSnapshot.child("comments").getChildrenCount() + "");
                        else
                            comments.add("0");
                        creatorID.add(dataSnapshot.child("creatorID").getValue(String.class));
                    }
                }else{
                    postID.clear();
                    title.clear();
                    email.clear();
                    name.clear();
                    email.clear();downvotes.clear();
                    upvotes.clear();
                }
                recyclerView.setAdapter(new PostsRecyclerAdapter(postID,creatorID,title,email,name,upvotes,downvotes,comments));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
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
                    postID.clear();
                    title.clear();
                    email.clear();
                    name.clear();
                    email.clear();downvotes.clear();
                    upvotes.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        postID.add(dataSnapshot.getKey());
                        title.add(dataSnapshot.child("title").getValue(String.class));
                        email.add(dataSnapshot.child("userEmail").getValue(String.class));
                        name.add(dataSnapshot.child("userName").getValue(String.class));
                        upvotes.add(dataSnapshot.child("upVotes").getValue(String.class));
                        downvotes.add(dataSnapshot.child("downVotes").getValue(String.class));
                        if(dataSnapshot.hasChild("comments"))
                            comments.add(dataSnapshot.child("comments").getChildrenCount() + "");
                        else
                            comments.add("0");
                        creatorID.add(dataSnapshot.child("creatorID").getValue(String.class));
                    }
                    recyclerView.setAdapter(new PostsRecyclerAdapter(postID,creatorID,title,email,name,upvotes,downvotes,comments));
                }else{
                    postID.clear();
                    title.clear();
                    email.clear();
                    name.clear();
                    email.clear();downvotes.clear();
                    upvotes.clear();
                    recyclerView.setAdapter(new PostsRecyclerAdapter(postID,creatorID,title,email,name,upvotes,downvotes,comments));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
