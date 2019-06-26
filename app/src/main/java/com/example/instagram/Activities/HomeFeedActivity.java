package com.example.instagram.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.instagram.Firebase.FirebaseMethods;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFeedActivity extends AppCompatActivity {
    private FirebaseMethods fbMethods;
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
