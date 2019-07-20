package com.example.instagram.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.instagram.R;

public class RegisterActivity extends AppCompatActivity {
    private Button continue_button;
    private EditText password;
    private EditText email;
    private Context current;
    private Button signUp;
    private EditText pass_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void toPartTwo(View view){
        Intent intent = new Intent(RegisterActivity.this, RegisterPartTwo.class);
        startActivity(intent);
    }
    public void previousActivity(View view) {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
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
