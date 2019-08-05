package com.example.instagram.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseMethods firebaseMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseMethods = new FirebaseMethods(this);
    }

    public void signOut(View view) {
        firebaseMethods.logout(LoginActivity.class);
    }

    public void back_button(View view) {
        Intent intent = new Intent(this, AccountProfile.class);
        startActivity(intent);
        finish();
    }
}
