package com.example.instagram.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;

public class PostLayout extends AppCompatActivity {
    private ImageView postImage;//
    private TextView topUsername;//
    private TextView bottomUsername;//
    private TextView comment;
    private ImageView profilePicture;//
    private ImageView heartButton;
    private ImageView convoButton;
    private TextView numberOfLikes;
    private TextView likes;
    private LinearLayout layout;
    private Integer id;
    //Userid
    private String userID;
    // Firebase
    private FirebaseMethods fbMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_layout);
        fbMethods = new FirebaseMethods(this);
        Intent intent = getIntent();
        userID = intent.getExtras().getString("UserID");
        id = Integer.parseInt(intent.getExtras().getString("identifier"));
        layout = findViewById(R.id.something);
        postImage = findViewById(R.id.post_image);
        topUsername = findViewById(R.id.top_username);
        bottomUsername = findViewById(R.id.bottom_username);
        comment = findViewById(R.id.comment);
        profilePicture = findViewById(R.id.profile_picture);
        heartButton = findViewById(R.id.like_button);
        convoButton = findViewById(R.id.conversation_button);
        likes = findViewById(R.id.number_of_likes);
        System.out.println("THIS IS MY ID <----------------> "+id);
        System.out.println("THIS IS MY userID <----------------> "+userID);
        fbMethods.search(profilePicture,null,this,userID);
        fbMethods.showPostInfor(userID,id,topUsername,bottomUsername,comment,likes,postImage);
        fbMethods.clickListenersPostLayout(heartButton,id);

    }

    public void backButton(View view) {
        Intent prevIntent = getIntent();
        if(prevIntent.getExtras().getString("activity").equals("class com.example.instagram.Activities.searchedUserProfile")){
            Intent intent = new Intent(this, searchedUserProfile.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
            finish();
        }
        if(prevIntent.getExtras().getString("activity").equals("class com.example.instagram.Activities.AccountProfile")){
            Intent intent = new Intent(this, AccountProfile.class);
            startActivity(intent);
            finish();
        }
        if(prevIntent.getExtras().getString("activity").equals("class com.example.instagram.Activities.searchActivity")){
            Intent intent = new Intent(this, searchActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void options(View view) {
    }
}
