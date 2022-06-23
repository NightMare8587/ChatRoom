package com.example.chatroom.Home.CreatePost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.UUID;

public class CreateNewPostActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    Button createPost;
    SharedPreferences sharedPreferences;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");
        editText = findViewById(R.id.createPostEditText);
        createPost = findViewById(R.id.createPostButton);
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        createPost.setOnClickListener(click -> {
            if(editText.length() != 0){
                CreatePostClass createPostClass = new CreatePostClass(editText.getText().toString(),sharedPreferences.getString("name",""),sharedPreferences.getString("email",""),"0","0",auth.getUid());
                UUID uuid = UUID.randomUUID();
                databaseReference.child(uuid.toString()).setValue(createPostClass);
                Toast.makeText(this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },620);
            }else {
                Toast.makeText(this, "Enter Something :)", Toast.LENGTH_SHORT).show();
                editText.requestFocus();
            }
        });
    }
}