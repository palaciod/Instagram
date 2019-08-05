package com.example.instagram.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.Models.HomeFeedRecyclerViewAdapter;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFeedActivity extends AppCompatActivity {
    private FirebaseMethods fbMethods;
    private ImageView insta_logo;
    private ImageView profile_button;
    private Parcelable listState;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private Parcelable state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fbMethods = new FirebaseMethods(HomeFeedActivity.this);
        fbMethods.sign_in_status(HomeFeedActivity.class);
        profile_button = findViewById(R.id.profile_picture);
        insta_logo = findViewById(R.id.logo);
        fbMethods.findProfilePicture(profile_button, null, this);
        Glide.with(HomeFeedActivity.this).load(R.drawable.ic_insta_letter_five).fitCenter().into(insta_logo);

        recyclerViewInit();


    }


    public void toPost(View view){
        Intent intent = new Intent(HomeFeedActivity.this, postActivity.class);
        startActivity(intent);
        finish();
    }
    public void toSearch(View view){
        Intent intent = new Intent(HomeFeedActivity.this, searchActivity.class);
        startActivity(intent);
        finish();
    }
    public void return_to_top(View view){
        recyclerViewInit();
    }

    // Temp method to logout
    public void log_out(View view){


    }
    public void letter_logo_touched(View view){
        insta_logo = findViewById(R.id.logo);
        insta_logo.setAlpha(0.5f);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                insta_logo.setAlpha(1f);
            }
        },100);
    }
    public void to_profile(View view){
        Intent intent = new Intent(HomeFeedActivity.this,AccountProfile.class);
        startActivity(intent);
        finish();
    }
    public void listInit(){

    }
    public void recyclerViewInit(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mFirebaseDatabase.getReference();
        final String userID = mAuth.getCurrentUser().getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, String> postList = new HashMap<>();
                for(DataSnapshot snap : dataSnapshot.child("following_list").child(userID).getChildren()){
                    // Going through the following list
                    System.out.println(snap.getKey());
                    String followingUserID = snap.getKey();
                    for(DataSnapshot ds: dataSnapshot.child("Posts").child(followingUserID).getChildren()){
                        System.out.println(ds.getKey());
                        postList.put(Integer.parseInt(ds.getKey().toString()), followingUserID);
                    }
                }
                // Really weird BUG, recycler resets scroll position when there is a change in a view.
                HomeFeedRecyclerViewAdapter adapter = new HomeFeedRecyclerViewAdapter(HomeFeedActivity.this, postList);
                recyclerView = findViewById(R.id.recycler);
                manager = new LinearLayoutManager(HomeFeedActivity.this, OrientationHelper.VERTICAL,false);
                recyclerView.setAdapter(adapter);

                recyclerView.setLayoutManager(manager);
                System.out.println();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fbMethods.onStart();
        System.out.println("<--------ON START!!!!---------->");
    }

    @Override
    public void onStop() {
        super.onStop();
        fbMethods.onStop();
        System.out.println("<----------ON STOP!!!!---------->");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("KeyForLayoutManagerState", manager.onSaveInstanceState());
        //outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        System.out.println(",-----------------------------------------save----------------------------------------------------->");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Parcelable state = savedInstanceState.getParcelable("KeyForLayoutManagerState");
        manager.onRestoreInstanceState(state);
        System.out.println(",------------------------------------------restore---------------------------------------------------->");

    }

    public void sample(View view) {
    }
}
