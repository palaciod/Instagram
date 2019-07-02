package com.example.instagram.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.instagram.Firebase.FirebaseMethods;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFeedActivity extends AppCompatActivity {
    private FirebaseMethods fbMethods;
    private ImageView insta_logo;
    private ImageView profile_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fbMethods = new FirebaseMethods(HomeFeedActivity.this);
        fbMethods.sign_in_status(HomeFeedActivity.class);
    }

    public void toPost(View view){

    }
    public void return_to_top(View view){

    }

    // Temp method to logout
    public void log_out(View view){
        fbMethods.logout(LoginActivity.class);

    }
    public void letter_logo_touched(View view){
        insta_logo = findViewById(R.id.insta_letter_logo);
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


}
