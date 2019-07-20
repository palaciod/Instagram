package com.example.instagram.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Activities.HomeFeedActivity;
import com.example.instagram.Activities.LoginActivity;
import com.example.instagram.Models.User;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.constraint.Constraints.TAG;

public class FirebaseMethods {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private Context myContext;
    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        myContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    private void toastMessage(String message){
        Toast.makeText(this.myContext,message,Toast.LENGTH_SHORT).show();
    }

    public void sign_in_status(final Class current_class){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(!myContext.getClass().equals(HomeFeedActivity.class)){
                        Intent intent = new Intent(myContext, current_class);
                        myContext.startActivity(intent);
                        toastMessage("Successfully signed in with: " + user.getEmail());
                        ((Activity) myContext).finish();
                    }


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

    }

    public void login(String user, String password, final Class current_class){
        mAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener((Activity) myContext, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.w(TAG, "signInWithEmail:failed", task.getException());

                    Toast.makeText((Activity) myContext, "Authentication Failed!",
                            Toast.LENGTH_SHORT).show();

                }else{
                    Log.d(TAG, "signInWithEmail: successful login");
                    Toast.makeText((Activity) myContext, "Authentication was successful!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, current_class);
                    myContext.startActivity(intent);
                    ((Activity) myContext).finish();
                }
            }
        });
    }

    public void logout(final Class next_class){
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        Toast.makeText((Activity) myContext,"You've been signed out.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(myContext,next_class);
        FirebaseAuth.getInstance().signOut();
        myContext.startActivity(intent);
        ((Activity) myContext).finish();


    }

    public void read_from_database(final TextView followerCount, final TextView followingCount, final TextView postCount){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String current_follower_count = ds.child(userID).child("followers").getValue().toString();
                    String current_following_count = ds.child(userID).child("following").getValue().toString();
                    String current_post_count = ds.child(userID).child("posts").getValue().toString();
                    followerCount.setText(current_follower_count);
                    followingCount.setText(current_following_count);
                    postCount.setText(current_post_count);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void onStart() {
        //super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        //super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}
