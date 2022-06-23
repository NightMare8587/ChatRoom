package com.example.chatroom.Home.Frags.Posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;

import java.util.ArrayList;
import java.util.List;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.Holder> {
    List<String> postID = new ArrayList<>();
    List<String> creatorID = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> email = new ArrayList<>();

    public PostsRecyclerAdapter(List<String> postID, List<String> creatorID, List<String> title, List<String> email, List<String> name, List<String> upvotes, List<String> downvotes) {
        this.postID = postID;
        this.creatorID = creatorID;
        this.title = title;
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
        View view = layoutInflater.inflate(R.layout.posts_cardview_layout,parent);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return postID.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        Button upVote,downVote,comment,report;
        TextView name,title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.postCreatorName);
            title = itemView.findViewById(R.id.postCreatorTitle);
            upVote = itemView.findViewById(R.id.upVoteButtonCard);
            downVote = itemView.findViewById(R.id.downVoteButtonCardView);
            comment = itemView.findViewById(R.id.commentButtonCardView);
            report = itemView.findViewById(R.id.reportButtonCardView);
        }
    }
}
