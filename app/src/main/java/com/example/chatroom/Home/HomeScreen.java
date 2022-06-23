package com.example.chatroom.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatroom.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class HomeScreen extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager2;
    TextView totalOnlineUsers;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference totalOnlineUsersNow;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        tabLayout = findViewById(R.id.tabLayoutHomeScreen);
        viewPager2 = findViewById(R.id.pager);
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        totalOnlineUsers = findViewById(R.id.totalOnlineUsersAvailable);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("OnlineUsers").child(auth.getUid());
        totalOnlineUsersNow = FirebaseDatabase.getInstance().getReference().getRoot().child("OnlineUsers");
        databaseReference.child("name").setValue(sharedPreferences.getString("name",""));
        databaseReference.child("email").setValue(sharedPreferences.getString("email",""));
        totalOnlineUsersNow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    totalOnlineUsers.setText("" + snapshot.getChildrenCount() + " Online Users");
                }else
                    totalOnlineUsers.setText("No User Available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Rooms"));
        tabLayout.addTab(tabLayout.newTab().setText("My Account"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager2.setAdapter(adapter);

        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager2.setOffscreenPageLimit(3);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            totalOnlineUsersNow.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        totalOnlineUsers.setText("" + snapshot.getChildrenCount() + " Online Users");
                                    }else
                                        totalOnlineUsers.setText("No User Available");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer = new Timer(); //This is new
        timer.schedule(timertask, 0, 2500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("OnlineUsers").child(auth.getUid());
        timer.cancel();
        databaseReference.removeValue();
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("OnlineUsers").child(auth.getUid());
        databaseReference.child("name").setValue(sharedPreferences.getString("name",""));
        databaseReference.child("email").setValue(sharedPreferences.getString("email",""));
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            totalOnlineUsersNow.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        totalOnlineUsers.setText("" + snapshot.getChildrenCount() + " Online Users");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer = new Timer(); //This is new
        timer.schedule(timertask, 0, 2500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("OnlineUsers").child(auth.getUid());
        timer.cancel();
        databaseReference.removeValue();
    }
}