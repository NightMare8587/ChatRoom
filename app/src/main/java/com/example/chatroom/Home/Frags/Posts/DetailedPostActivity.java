package com.example.chatroom.Home.Frags.Posts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class DetailedPostActivity extends AppCompatActivity {
    String upVotes,downVotes,email,name,creatorID,postID,title;
    TextView namePost,titlePost;
    Button addComment;
    RecyclerView recyclerView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    List<String> commentEmail = new ArrayList<>();
    List<String> commentName = new ArrayList<>();
    List<String> comment = new ArrayList<>();
    List<String> authIdComment = new ArrayList<>();
    List<String> timeAdded = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);
        initialise();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        timeAdded.add(dataSnapshot.getKey());
                        commentName.add(String.valueOf(dataSnapshot.child("name").getValue()));
                        commentEmail.add(String.valueOf(dataSnapshot.child("email").getValue()));
                        comment.add(String.valueOf(dataSnapshot.child("comment").getValue()));
                        authIdComment.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                    }
                    recyclerView.setAdapter(new DetailedRecycler(commentEmail,commentName,comment,authIdComment,timeAdded));
                } else {
                    Toast.makeText(DetailedPostActivity.this, "No Comments", Toast.LENGTH_SHORT).show();
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

        addComment.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(click.getContext());
            builder.setTitle("Dialog").setMessage("Add your comment in below field");
            LinearLayout linearLayout = new LinearLayout(click.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            EditText editText = new EditText(click.getContext());
            editText.setHint("Enter Here");
            editText.setMaxLines(200);
            linearLayout.addView(editText);

            builder.setPositiveButton("Add Comment", (dialog, which) -> {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(DetailedPostActivity.this, "Enter Something :)", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID).child("comments");
                String time = System.currentTimeMillis() + "";
                SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
                reference.child(time).child("name").setValue(sharedPreferences.getString("name",""));
                reference.child(time).child("email").setValue(sharedPreferences.getString("email",""));

                reference.child(time).child("authID").setValue(auth.getUid());
                reference.child(time).child("comment").setValue(editText.getText().toString());
                Toast.makeText(this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();

            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            builder.setView(linearLayout);
            builder.show();
        });


    }

    private void updateChild() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    timeAdded.clear();
                    comment.clear();
                    commentEmail.clear();
                    authIdComment.clear();
                    commentName.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        timeAdded.add(dataSnapshot.getKey());
                        commentName.add(String.valueOf(dataSnapshot.child("name").getValue()));
                        commentEmail.add(String.valueOf(dataSnapshot.child("email").getValue()));
                        comment.add(String.valueOf(dataSnapshot.child("comment").getValue()));
                        authIdComment.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                    }
                } else {
                    timeAdded.clear();
                    comment.clear();
                }
                recyclerView.setAdapter(new DetailedRecycler(commentEmail,commentName,comment,authIdComment,timeAdded));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialise() {
        upVotes = getIntent().getStringExtra("upVotes");
        downVotes = getIntent().getStringExtra("downVotes");
        title = getIntent().getStringExtra("title");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        postID = getIntent().getStringExtra("postID");
        creatorID = getIntent().getStringExtra("creatorID");
        recyclerView = findViewById(R.id.commentsRecyclerViewDetailedPost);
        namePost = findViewById(R.id.nameOfCreatorDetailedPost);
        titlePost = findViewById(R.id.titlePostDetailed);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID).child("comments");
        namePost.setText(name);
        addComment = findViewById(R.id.addAComentButtonDetailed);
        titlePost.setText(title);

    }
}