package com.example.instagram.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.Firebase.FirebaseMethods;
import com.example.instagram.R;

public class LoginActivity extends AppCompatActivity {
    private static final String Tag = "LoginActivity";
    private EditText username;
    private EditText password;
    private FirebaseMethods mFirebaseMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        String user = username.toString();
        String pass = password.toString();
        mFirebaseMethods = new FirebaseMethods(LoginActivity.this);
        mFirebaseMethods.sign_in_status(HomeFeedActivity.class);
    }

    public boolean areEditTextsFilled(String username,String password){
        if(username.isEmpty()||password.isEmpty()){
            return false;
        }
        return true;
    }

    public void login(View view) {
        if(areEditTextsFilled(username.getText().toString().trim(),password.getText().toString().trim())){
            mFirebaseMethods.login(username.getText().toString().trim(),password.getText().toString().trim(),HomeFeedActivity.class);

        }else{
            Toast.makeText(LoginActivity.this, "Please fill in the required texts.",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseMethods.onStart();
        System.out.println("<--------ON START!!!!---------->");
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseMethods.onStop();
        System.out.println("<----------ON STOP!!!!---------->");
    }




}
