package com.example.chatroom.Home.Frags.Posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;

import java.util.ArrayList;
import java.util.List;

public class DetailedRecycler extends RecyclerView.Adapter<DetailedRecycler.Holder> {
    List<String> commentEmail = new ArrayList<>();
    List<String> commentName = new ArrayList<>();
    List<String> comment = new ArrayList<>();
    List<String> authIdComment = new ArrayList<>();
    List<String> timeAdded = new ArrayList<>();

    public DetailedRecycler(List<String> commentEmail, List<String> commentName, List<String> comment, List<String> authIdComment, List<String> timeAdded) {
        this.commentEmail = commentEmail;
        this.commentName = commentName;
        this.comment = comment;
        this.authIdComment = authIdComment;
        this.timeAdded = timeAdded;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_card_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.comment.setText(comment.get(position));
        holder.name.setText(commentName.get(position));
    }

    @Override
    public int getItemCount() {
        return timeAdded.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        TextView name,comment;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentNameRecycler);
            comment = itemView.findViewById(R.id.commentActualRecyclerVuew);
        }
    }
}
