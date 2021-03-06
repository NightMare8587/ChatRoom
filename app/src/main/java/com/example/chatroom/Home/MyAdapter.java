package com.example.chatroom.Home;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatroom.Home.Frags.MyAccountFragment;
import com.example.chatroom.Home.Frags.Posts.PostsFragment;
import com.example.chatroom.Home.Frags.Rooms.RoomsFragment;

class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PostsFragment buyBooksFrag = new PostsFragment();
                return buyBooksFrag;
            case 1:
                RoomsFragment myAccountFrag = new RoomsFragment();
                return myAccountFrag;
            case 2:
                 MyAccountFragment myAccountFragment = new MyAccountFragment();
                return myAccountFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}