package com.example.instagram.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.instagram.Models.RecyclerViewAdapter;
import com.example.instagram.searchFragment;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;
import com.example.instagram.post_description;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class searchActivity extends AppCompatActivity implements searchFragment.OnFragmentInteractionListener{
    private FirebaseMethods fbMethods;
    private ImageView profilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fbMethods = new FirebaseMethods(searchActivity.this);
        profilePicture = findViewById(R.id.profile_picture);
        fbMethods.findProfilePicture(profilePicture, null, this);
        recyclerViewInit();
    }

    public void showSuggestions(View view) {
        searchFragment frag = new searchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_container,frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void hideKeyboard(){
        if(getCurrentFocus()!= null){
            InputMethodManager nm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            nm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void toHomeFeed(View view) {
        Intent intent = new Intent(searchActivity.this, HomeFeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void return_to_top(View view) {
    }

    public void toPost(View view) {
        Intent intent  = new Intent(searchActivity.this, postActivity.class);
        startActivity(intent);
        finish();
    }

    public void to_profile(View view) {
        Intent intent  = new Intent(searchActivity.this, AccountProfile.class);
        startActivity(intent);
        finish();
    }

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
                ArrayList<String> userIDList = new ArrayList<>();
                ArrayList<ArrayList<String>> userIDListHolder = new ArrayList<>();
                int tracker = 0;
                for (DataSnapshot ds : dataSnapshot.child("random_search_post_list").getChildren()){
                    System.out.println("THIS IS DS: ====== " + ds);

                    identifierList.add(Integer.parseInt(ds.getKey()));
                    userIDList.add(ds.child("userID").getValue().toString());
                    if(identifierList.size()==3){
                        ArrayList<Integer> temp = new ArrayList<>();
                        ArrayList<String> userTemp = new ArrayList<>();

                        temp.add(identifierList.get(0));

                        userTemp.add(userIDList.get(0));

                        temp.add(identifierList.get(1));

                        userTemp.add(userIDList.get(1));

                        temp.add(identifierList.get(2));

                        userTemp.add(userIDList.get(2));

                        identifierListHolder.add(temp);
                        userIDListHolder.add(userTemp);
                        identifierList.clear();
                        userIDList.clear();
                    }
                    System.out.println("This is tracker" + tracker);
                    System.out.println("This is tracker" + identifierList.size());
                    tracker++;
                    if(tracker == dataSnapshot.child("random_search_post_list").getChildrenCount()){
                        if(identifierList.size()!= 0){
                            identifierListHolder.add(identifierList);
                            userIDListHolder.add(userIDList);
                        }
                    }else{
                        //System.out.println("FALSEEEEEEEEEEEEEEEE and this is the size of get childeren count: "+ dataSnapshot.child("Posts").child(userID).getChildrenCount());
                    }


                }

                // Here is where we will call our initRecyclerView method
                System.out.println("This is of the arrayList <------------------> " + identifierListHolder.size());
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(identifierListHolder,searchActivity.this,userID,userIDListHolder);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(searchActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d(TAG,"recyclerViewInit: init");
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
