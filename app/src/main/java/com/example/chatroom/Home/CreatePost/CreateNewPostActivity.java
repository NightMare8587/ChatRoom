package com.example.chatroom.Home.CreatePost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewPostActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    Button createPost;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");
        editText = findViewById(R.id.createPostEditText);
        createPost = findViewById(R.id.createPostButton);

        createPost.setOnClickListener(click -> {
            
        });
    }
}