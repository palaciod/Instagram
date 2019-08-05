package com.example.instagram.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;

import java.util.ArrayList;

public class EditProfilePage extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 5678;
    private ImageView profilePicture;
    private TextView name;
    private EditText editName;
    private TextView username;
    private EditText editUsername;
    private TextView bio;
    private EditText editBio;
    private TextView phone;
    private EditText editPhone;
    private EditText editGender;
    private EditText editEmail;
    private Uri selectedImageUri;

    // FireBase
    private FirebaseMethods firebaseMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        profilePicture = findViewById(R.id.profile_picture);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editBio);
        editGender = findViewById(R.id.editGender);
        editEmail = findViewById(R.id.editEmail);
        firebaseMethods = new FirebaseMethods(EditProfilePage.this);
        firebaseMethods.showProfileDetails(editName,editUsername,editEmail,editBio,editPhone,editGender);
        firebaseMethods.findProfilePicture(profilePicture,null, this);
    }

    public void cancel(View view) {
        Intent intent = new Intent(EditProfilePage.this, AccountProfile.class);
        startActivity(intent);
        finish();
    }

    /**
     * Insert Firebase method
     * @param view
     */
    public void changeProfilePicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            selectedImageUri = data.getData();
            Glide.with(EditProfilePage.this).load(selectedImageUri).centerCrop().apply(RequestOptions.circleCropTransform()).into(profilePicture);
        }
    }

    /**
     * The done button that will update the profile
     * @param view
     */
    public void done(View view) {
        ArrayList<String> changeStatus = new ArrayList<>();
        if(selectedImageUri != null){
            firebaseMethods.uploadPictureFromLibrary(selectedImageUri);
        }
        firebaseMethods.updateProfile(editName,editUsername,editEmail,editBio,editPhone,editGender,this, changeStatus);
        if(changeStatus.get(0).equals("true")){
            Intent intent = new Intent(this, AccountProfile.class);
            startActivity(intent);
            finish();
        }

    }
}
