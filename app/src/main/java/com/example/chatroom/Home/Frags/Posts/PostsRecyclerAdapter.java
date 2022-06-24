package com.example.chatroom.Home.Frags.Posts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.Holder> {
    List<String> postID = new ArrayList<>();
    List<String> creatorID = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> email = new ArrayList<>();
    List<String> comments = new ArrayList<>();

    public PostsRecyclerAdapter(List<String> postID, List<String> creatorID, List<String> title, List<String> email, List<String> name,
                                List<String> upvotes, List<String> downvotes,List<String> comments) {
        this.postID = postID;
        this.creatorID = creatorID;
        this.title = title;
        this.comments = comments;
        this.email = email;
        this.name = name;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    List<String> name = new ArrayList<>();
    List<String> upvotes = new ArrayList<>();
    List<String> downvotes = new ArrayList<>();
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.posts_cardview_layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText(title.get(position));
        holder.name.setText(name.get(position));
        holder.upVote.setText(upvotes.get(position));
        holder.downVote.setText(downvotes.get(position));

        holder.comment.setText(comments.get(position));


        holder.upVote.setOnClickListener(click -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position)).child("TotalUpVotes");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if(snapshot.hasChild(auth.getUid())){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int current = Integer.parseInt(String.valueOf(snapshot.child("upVotes").getValue()));
                                    current -= 1;
                                    Toast.makeText(click.getContext(), "Upvote Removed", Toast.LENGTH_SHORT).show();
                                    reference.child("upVotes").setValue(current + "");
                                    databaseReference.child(auth.getUid()).removeValue();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int current = Integer.parseInt(String.valueOf(snapshot.child("upVotes").getValue()));
                                    current += 1;
                                    Toast.makeText(click.getContext(), "Post UpVoted", Toast.LENGTH_SHORT).show();
                                    reference.child("upVotes").setValue(current + "");
                                    databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }else{
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int current = Integer.parseInt(String.valueOf(snapshot.child("upVotes").getValue()));
                                current += 1;
                                Toast.makeText(click.getContext(), "Post UpVoted", Toast.LENGTH_SHORT).show();
                                databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                                reference.child("upVotes").setValue(current + "");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        holder.downVote.setOnClickListener(click -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position)).child("TotalDownVotes");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if(snapshot.hasChild(auth.getUid())){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int current = Integer.parseInt(String.valueOf(snapshot.child("downVotes").getValue()));
                                    current -= 1;
                                    databaseReference.child(auth.getUid()).removeValue();
                                    Toast.makeText(click.getContext(), "DownVoted Removed", Toast.LENGTH_SHORT).show();
                                    reference.child("downVotes").setValue(current + "");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int current = Integer.parseInt(String.valueOf(snapshot.child("downVotes").getValue()));
                                    current += 1;
                                    databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                                    Toast.makeText(click.getContext(), "DownVoted", Toast.LENGTH_SHORT).show();
                                    reference.child("downVotes").setValue(current + "");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }else{
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts").child(postID.get(position));
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int current = Integer.parseInt(String.valueOf(snapshot.child("downVotes").getValue()));
                                current += 1;
                                Toast.makeText(click.getContext(), "DownVoted", Toast.LENGTH_SHORT).show();
                                reference.child("downVotes").setValue(current + "");
                                databaseReference.child(auth.getUid()).child("authID").setValue(auth.getUid());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        holder.report.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(click.getContext());
            builder.setTitle("What Happened").setMessage("Explain briefly why you want to report this post");
            LinearLayout linearLayout = new LinearLayout(click.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            EditText editText = new EditText(click.getContext());
            editText.setHint("Enter Here");
            editText.setMaxLines(200);
            linearLayout.addView(editText);
            builder.setPositiveButton("Report", (dialog, which) -> {
                SharedPreferences sharedPreferences = click.getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Reports").child(postID.get(position));
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int current = Integer.parseInt(String.valueOf(snapshot.child("totalReports").getValue()));
                            current += 1;
                            databaseReference.child("totalReports").setValue(current);
                        }else{
                            databaseReference.child("reportedByName").setValue(sharedPreferences.getString("name",""));
                            databaseReference.child("reportedByEmail").setValue(sharedPreferences.getString("email",""));
                            databaseReference.child("creatorID").setValue(creatorID.get(position));
                            databaseReference.child("title").setValue(title.get(position));
                            databaseReference.child("name").setValue(name.get(position));
                            databaseReference.child("totalReports").setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }).setNegativeButton("Cancel", (dialog, which) -> {

            });
            builder.setView(linearLayout);
            builder.create().show();
        });

        holder.cardView.setOnClickListener(click -> {
            Intent intent = new Intent(click.getContext(),DetailedPostActivity.class);
            intent.putExtra("postID",postID.get(position));
            intent.putExtra("name",name.get(position));
            intent.putExtra("creatorID",creatorID.get(position));
            intent.putExtra("title",title.get(position));
            intent.putExtra("upVotes",upvotes.get(position));
            intent.putExtra("downVotes",downvotes.get(position));
            intent.putExtra("email",email.get(position));
            click.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return postID.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        Button upVote,downVote,comment,report;
        TextView name,title;
        CardView cardView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.postCreatorName);
            title = itemView.findViewById(R.id.postCreatorTitle);
            upVote = itemView.findViewById(R.id.upVoteButtonCard);
            downVote = itemView.findViewById(R.id.downVoteButtonCardView);
            comment = itemView.findViewById(R.id.commentButtonCardView);
            cardView = itemView.findViewById(R.id.postCardViewID);
            report = itemView.findViewById(R.id.reportButtonCardView);
        }
    }
}
