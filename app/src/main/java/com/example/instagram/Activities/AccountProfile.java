package com.example.instagram.Activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.Models.RecyclerViewAdapter;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;

public class AccountProfile extends AppCompatActivity implements RecyclerViewAdapter.postCountListener {
    private static final String TAG = "AccountProfile: ";
    private TextView post_count;
    private TextView follower_count;
    private TextView following_count;
    private FirebaseMethods mFirebaseMethods;
    private TextView username;
    private ImageView profilePicture;
    private ImageView main_profilePicture;
    // Testing list
    private TextView bio;
    private ArrayList<ArrayList<Uri>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);
        follower_count = findViewById(R.id.followers_count);
        following_count = findViewById(R.id.following_count);
        post_count = findViewById(R.id.posts_count);
        username = findViewById(R.id.username);
        list = new ArrayList<>();
        profilePicture = findViewById(R.id.profile_picture);
        main_profilePicture = findViewById(R.id.main_profile_picture_account);
        mFirebaseMethods = new FirebaseMethods(AccountProfile.this);
        mFirebaseMethods.read_from_database(follower_count,following_count,post_count,username);
        mFirebaseMethods.findProfilePicture(profilePicture, main_profilePicture, this);
        bio = findViewById(R.id.description);
        mFirebaseMethods.getDescription(bio);
        recyclerViewInit();
    }

    public void edit_profile_button(View view){
        Intent intent = new Intent(AccountProfile.this, EditProfilePage.class);
        startActivity(intent);
        finish();
    }
    public void toSearch(View view){
        Intent intent = new Intent(AccountProfile.this, searchActivity.class);
        startActivity(intent);
        finish();
    }
    public void toHomeFeed(View view){
        Intent intent = new Intent(AccountProfile.this,HomeFeedActivity.class);
        startActivity(intent);
    }
    public void toPost(View view) {
        Intent intent = new Intent(AccountProfile.this, postActivity.class);
        startActivity(intent);
        finish();
    }
    public void printPath(View view){
        //mFirebaseMethods.findProfilePicture();
    }
    // Testing methods
    public void addToImageUrl(){
        ArrayList<Uri> insideList = new ArrayList<>();
        Uri firstUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/instagram-3d782.appspot.com/o/posts%2Fusers%2FnKRZkaioTlgcSqNdkcvLCvILABt2%2F2063002052?alt=media&token=b4db8e6e-6736-4db2-be5c-3dd5ee00250e");
        insideList.add(firstUri);
        Uri secondUri = Uri.parse("https://secure.i.telegraph.co.uk/multimedia/archive/03037/Radamel-Falcao_3037895b.jpg");
        insideList.add(secondUri);
        Uri thirdUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/instagram-3d782.appspot.com/o/posts%2Fusers%2FnKRZkaioTlgcSqNdkcvLCvILABt2%2F507914743?alt=media&token=6aeaebfe-41d1-44c7-b9e4-e2582535aec0");
        insideList.add(thirdUri);
        System.out.println("This is my first key: " + insideList.get(0));
        list.add(insideList);
        //initRecyclerView();
    }
//    private void initRecyclerView(){
//        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list,this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }

    /**
     * Put this into Firebasmethod class later.
     */
    public void recyclerViewInit(){
       FirebaseAuth mAuth = FirebaseAuth.getInstance();
       FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        final String userID = mAuth.getCurrentUser().getUid();
       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ArrayList<ArrayList<Integer>> identifierListHolder = new ArrayList<>();
               ArrayList<Integer> identifierList = new ArrayList<>();

               // The arraylist below will not be used within the account profile, since userID will be constant throughout the iteration of the datasnapshot.

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
               RecyclerViewAdapter adapter = new RecyclerViewAdapter(identifierListHolder,AccountProfile.this,userID, userIDList);
               recyclerView.setAdapter(adapter);
               recyclerView.setLayoutManager(new LinearLayoutManager(AccountProfile.this));
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"recyclerViewInit: init");
           }
       });
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
    }


    @Override
    public int getPostCount() {
        return Integer.parseInt(post_count.getText().toString());
    }


    public void toSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
