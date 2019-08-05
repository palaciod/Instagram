package com.example.instagram.Activities;
import java.time.LocalDateTime;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.Models.Post;
import com.example.instagram.R;
import com.example.instagram.post_description;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

public class postActivity extends AppCompatActivity implements post_description.OnFragmentInteractionListener {
    private static final String TAG = "postActivity";
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 5678;
    private ImageView postImage;
    private FirebaseMethods firebaseMethods;
    private FragmentManager fragmentManager;
    private Post post;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postImage = findViewById(R.id.postImage);
        firebaseMethods = new FirebaseMethods(postActivity.this);
        fragmentManager = getSupportFragmentManager();

    }

    public void toHomeFeed(View view) {
        Intent intent = new Intent(postActivity.this, HomeFeedActivity.class);
        startActivity(intent);
        finish();
    }
    public void toDescriptionFragment(View view){
        post_description post_frag = new post_description();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.post_container,post_frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Results when selecting a new image from memory
     * @param view -  The view from which the TextView is from.
     */
    public void chooseFromLibrary(View view){
        Log.d(TAG, "onClick: accessing phones memory.");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }
    /**
     * Results when taking a new photo with camera
     * @param view -  The view from which the TextView is from.
     */
    public void takeAPicture(View view){
        Log.d(TAG, "onClick: starting camera.");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            /*
            Results when selecting a new image from memory
             */
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            selectedImageUri = data.getData();
            Glide.with(postActivity.this).load(selectedImageUri).into(postImage);
            //postImage.setImageURI(selectedImageUri);
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR);
            int minutes = cal.get(Calendar.MINUTE);
            String date = year + "/" + month + "/" + day + "/" + hour + "/" + minutes;
            post = new Post(null,0, firebaseMethods.randomUniqueIdentifier(), firebaseMethods.getUserID(), date);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Post Object", (Serializable) post);
            /*
            Results when taking a new photo with camera
            */
        }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            Glide.with(postActivity.this).load(bitmap).into(postImage);
            post = new Post(bitmap,null,0, new HashMap<String, String>());
        }


    }


    @Override
    public void sendUri(Post _post) {
        post = post;
    }

    @Override
    public Uri getUri() {
        return selectedImageUri;
    }
    @Override
    public Post getPost() {
        return post;
    }
    @Override
    public FirebaseMethods getFirebaseMethods(){
        return firebaseMethods;
    }
}
