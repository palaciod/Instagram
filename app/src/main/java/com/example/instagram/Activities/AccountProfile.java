package com.example.instagram.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;

public class AccountProfile extends AppCompatActivity {
    private TextView post_count;
    private TextView follower_count;
    private TextView following_count;
    private FirebaseMethods mFirebaseMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);
        mFirebaseMethods = new FirebaseMethods(AccountProfile.this);
        follower_count = findViewById(R.id.followers_count);
        following_count = findViewById(R.id.following_count);
        post_count = findViewById(R.id.posts_count);
        mFirebaseMethods.read_from_database(follower_count,following_count,post_count);
    }

    public void edit_profile_button(View view){

    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
}
