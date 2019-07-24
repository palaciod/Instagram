package com.example.instagram.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadProfilePicture extends AppCompatActivity {
    private static final String TAG = "UploadProfilePicture";
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 5678;
    private ImageView profilePicture;
    private TextView photo_library;
    private FirebaseMethods fbMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);
        profilePicture = findViewById(R.id.profile_picture);
        photo_library = findViewById(R.id.photo_library);
        fbMethods = new FirebaseMethods(UploadProfilePicture.this);
        photo_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
            Results when selecting a new image from memory
         */
                Log.d(TAG, "onClick: accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            /*
            Results when selecting a new image from memory
             */
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image uri: " + selectedImageUri);
            System.out.println("This is my image url: " + selectedImageUri);
            profilePicture.setImageURI(selectedImageUri);
            fbMethods.uploadPictureFromLibrary(selectedImageUri);

            /*
            Results when taking a new photo with camera
            */
        }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking new photo");
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data"); 
            profilePicture.setImageBitmap(bitmap);

        }


    }

    public void take_a_picture(View view){
        Log.d(TAG, "onClick: starting camera.");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    public void toHomePage(View view){
        Intent intent = new Intent(UploadProfilePicture.this, HomeFeedActivity.class);
        startActivity(intent);
        finish();
    }

}
