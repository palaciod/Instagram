package com.example.instagram.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.Models.RecyclerViewAdapter;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class searchedUserProfile extends AppCompatActivity {
    private static final String TAG = "searchedUserProfile: ";
    private FirebaseMethods firebaseMethods;
    private ImageView mainProfilePicture;
    private ImageView bottomProfilePicture;
    private TextView followerCount;
    private TextView followingCount;
    private TextView postCount;
    private TextView usernameText;
    private String userID;
    private Button followButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_user_profile);
        firebaseMethods = new FirebaseMethods(searchedUserProfile.this);
        mainProfilePicture = findViewById(R.id.main_profile_picture_account);
        bottomProfilePicture = findViewById(R.id.bottom_profile_picture);
        followerCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        postCount = findViewById(R.id.posts_count);
        usernameText = findViewById(R.id.username);
        followButton = findViewById(R.id.followButton);
        toMyAccountProfile();
        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        // FireBase Methods
        firebaseMethods.searchProfilePicture(mainProfilePicture, bottomProfilePicture, searchedUserProfile.this, userID);
        firebaseMethods.getSearchUsername(followerCount,followingCount,postCount,usernameText,userID);
        firebaseMethods.stateFollowButton(userID, followButton);
        firebaseMethods.searchedProfilePicture(bottomProfilePicture,this);
        recyclerViewInit();
    }

    /**
     * Don't know why bottomProfilePicture wasn't recognized in user_search_activity.
     */
    public void toMyAccountProfile(){
        bottomProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchedUserProfile.this, AccountProfile.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Firebase methods will be put in here
     * @param view
     */
    public void followButton(View view){
        firebaseMethods.follow(userID, followButton);
    }
    public void backButton(View view){
        Intent intent = new Intent(searchedUserProfile.this, searchActivity.class);
        startActivity(intent);
        finish();
    }
    public void toPost(View view){
        Intent intent = new Intent(searchedUserProfile.this, postActivity.class);
        startActivity(intent);
        finish();
    }
    public void toHomeFeed(View view){
        Intent intent = new Intent(searchedUserProfile.this, HomeFeedActivity.class);
        startActivity(intent);
        finish();
    }
    public void toSearch(View view){
        Intent intent = new Intent(searchedUserProfile.this, searchActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This is method is perfect on it's own to be moved to FirebaseMethods. Only have to take userID as an argument.
     */

    public void recyclerViewInit(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<Integer>> identifierListHolder = new ArrayList<>();
                ArrayList<Integer> identifierList = new ArrayList<>();
                ArrayList<ArrayList<String>> userIDList = new ArrayList<>();
                int tracker = 0;
                for (DataSnapshot ds : dataSnapshot.child("Posts").child(userID).getChildren()){
                    System.out.println("THIS IS DS: ====== " + ds);

                    identifierList.add(Integer.parseInt(ds.getKey()));
                    if(identifierList.size()==3 && !(tracker== dataSnapshot.child("Posts").child(userID).getChildrenCount())){
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(identifierList.get(0));
                        temp.add(identifierList.get(1));
                        temp.add(identifierList.get(2));
                        identifierListHolder.add(temp);
                        identifierList.clear();
                    }
                    System.out.println("This is tracker" + tracker);
                    System.out.println("This is tracker" + identifierList.size());
                    tracker++;
                    if(tracker== dataSnapshot.child("Posts").child(userID).getChildrenCount()){
                        if(identifierList.size()!= 0){
                            identifierListHolder.add(identifierList);
                        }
                    }else{
                        System.out.println("FALSEEEEEEEEEEEEEEEE and this is the size of get childeren count: "+ dataSnapshot.child("Posts").child(userID).getChildrenCount());
                    }


                }

                // Here is where we will call our initRecyclerView method
                System.out.println("This is of the arrayList <------------------> " + identifierListHolder.size());
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(identifierListHolder,searchedUserProfile.this,userID,userIDList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(searchedUserProfile.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"recyclerViewInit: init");
            }
        });
    }
}
