package com.example.instagram.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Activities.HomeFeedActivity;
import com.example.instagram.Activities.LoginActivity;
import com.example.instagram.Activities.RegisterActivity;
import com.example.instagram.Activities.RegisterPartTwo;
import com.example.instagram.Models.User;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.support.constraint.Constraints.TAG;

public class FirebaseMethods {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private Context myContext;
    private StorageReference mStorageRef;
    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        myContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }
    public String getEmail(){
        return mAuth.getCurrentUser().getEmail();
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
    public void write_to_database(String name, String birthday, int age,  String phoneNumber, String gender, String username){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    System.out.println("User is signed in and its: "+ user.getUid());
                    System.out.println("User is: "+ user.getEmail());
                }else{
                    System.out.println("Sign in failed.");
                }
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("<------------------------------------------>");
                System.out.println("On Data Change: Information has been added to FireBase: "+ "---"+dataSnapshot.getValue()+"---");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to reach data, error: "+databaseError.toException());
            }
        });
        User user = new User(name, birthday, age, phoneNumber, gender, username);
        myRef.child("user_account_details").child(userID).setValue(user);
    }


    public void registerEmailPassword( String email, String password, String pass_confirm){
        /*
                ------------------ First If statement handles the empty string exception---------------------
                 */
        if(email.equals("")|| password.equals("")|| pass_confirm.equals("")){
            System.out.println("Incorrect Input type");
            Toast.makeText(myContext,"You must fill out all the required fields!",Toast.LENGTH_SHORT).show();
        }
                /*
                -------------------Second If statement handles if the passwords match------------------------
                 */
        if(!password.equals(pass_confirm)){
            System.out.println("Passwords Do Not Match!");
            Toast.makeText( myContext,"Passwords do not match!",Toast.LENGTH_SHORT).show();
        }
        /*
                --------------------Finally the else statement handles the create user function and starts a new intent--------------------
        */
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) myContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(myContext, RegisterPartTwo.class);
                        //user.getEmail(emailString);
                        myContext.startActivity(intent);
                        ((Activity) myContext).finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(myContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

//        Intent intent = new Intent(RegisterActivity.this, RegisterPartTwo.class);
//        startActivity(intent);
    }

    public void uploadPictureFromLibrary (Uri selectedImage){

        StorageReference storageReference = mStorageRef.child("profilePictures/users/" + userID + "/" );

        storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                toastMessage("Upload Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage("Upload Failed");
            }
        });
    }
    public void uploadPictureFromCamera(Bitmap photoTaken){
        
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
