package com.example.instagram.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText password;
    private EditText email;
    private Context current;
    private Button signUp;
    private EditText pass_confirm;
    private FirebaseMethods fbMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        pass_confirm = findViewById(R.id.password_confirmation);
        fbMethods = new FirebaseMethods(RegisterActivity.this);
    }

    public void toPartTwo(View view){
        String email_string = email.getText().toString().trim();
        String password_string = password.getText().toString().trim();
        String pass_confirm_string = pass_confirm.getText().toString().trim();
        System.out.println(email_string);
        fbMethods.registerEmailPassword(email_string, password_string, pass_confirm_string);
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
