package com.example.chatroom.Home.Frags;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatroom.Home.CreatePost.CreateNewPostActivity;
import com.example.chatroom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostsFragment extends Fragment {
    FloatingActionButton floatingActionButton;
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
    }
}
