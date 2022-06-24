package com.example.chatroom.Home.Frags.Rooms.JoinRoom;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter {
    List<String> time = new ArrayList<>();
    List<String> message = new ArrayList<>();
    List<String> senderID = new ArrayList<>();
    List<String> name = new ArrayList<>();

    public RoomAdapter(List<String> time, List<String> message, List<String> senderID, List<String> name) {
        this.time = time;
        this.message = message;
        this.senderID = senderID;
        this.name = name;
    }

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == 0){
            view = layoutInflater.inflate(R.layout.card_message_revive,parent,false);
            return new ReciveHolder(view);
        }else{
            view = layoutInflater.inflate(R.layout.card_message_send,parent,false);
            return new SentViewHolder(view);
        }
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        TextView send;
        Button sendImage;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            send = itemView.findViewById(R.id.textSend);

        }
    }

    public class ReciveHolder extends RecyclerView.ViewHolder{
        TextView recive;
        public ReciveHolder(@NonNull View itemView) {
            super(itemView);
            recive = itemView.findViewById(R.id.textRecive);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;
            ((SentViewHolder) holder).send.setText(message.get(position));
            ((SentViewHolder) holder).send.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager)v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(((SentViewHolder) holder).send.getText().toString());
                    Toast.makeText(v.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }else{
            ReciveHolder reciveHolder = (ReciveHolder) holder;
            ((ReciveHolder) holder).recive.setText(message.get(position));
            ((ReciveHolder) holder).recive.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager)v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(((ReciveHolder) holder).recive.getText().toString());
                    Toast.makeText(v.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(auth.getUid().equals(senderID.get(position)))
            return 0;
        else
            return 1;
//        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return time.size();
    }

}
