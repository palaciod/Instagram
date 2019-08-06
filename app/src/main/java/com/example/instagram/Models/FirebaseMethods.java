package com.example.instagram.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

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
    public String getUserName(){
        String username = "";
        String email = getEmail();
        for (int i = 0; i < email.length();i++){
            if(email.charAt(i) == '@'){
                break;
            }
            username = username + email.charAt(i);
        }
        return username;
    }

    public String getUserID(){
        return userID;
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
    public void getDescription( final TextView bio, String _userID){
        myRef.child("user_account_details").child(_userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String _bio = dataSnapshot.child("profileDescription").getValue().toString();
                bio.setText(_bio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public int randomUniqueIdentifier(){
        Random r = new Random();
        return r.nextInt(2147483647);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


    }

    public void read_from_database(final TextView followerCount, final TextView followingCount, final TextView postCount, final TextView usernameTextView){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String current_follower_count = Long.toString(dataSnapshot.child("follower_list").child(userID).getChildrenCount());
                String current_following_count = Long.toString(dataSnapshot.child("following_list").child(userID).getChildrenCount());
                long current_post_count = dataSnapshot.child("Posts").child(userID).getChildrenCount();
                String username = dataSnapshot.child("user_account_details").child(userID).child("userName").getValue().toString();
                followerCount.setText(current_follower_count);
                followingCount.setText(current_following_count);
                postCount.setText(Long.toString(current_post_count));
                usernameTextView.setText(username);
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
        User user = new User(name, birthday, age, phoneNumber, gender, username,0,0,0,"");
        myRef.child("user_account_details").child(userID).setValue(user);
    }

    /**
     * A method that will add to the followers list for the searched username and increment the following list for the authenticated user.
     * @param _userID
     * @param followButton
     */
    public void follow(final String _userID, Button followButton){
        //myRef.child("follower_list").child(_userID).setValue(null);
        myRef.child("user_account_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = userID;
                String secondUsername = _userID;
                Followers follower = new Followers(username);
                myRef.child("follower_list").child(_userID).child(userID).setValue(follower);
                Following following = new Following(secondUsername);
                myRef.child("following_list").child(userID).child(_userID).setValue(following);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Follow button does not appear blue when the user is not following
     * @param _userID
     * @param followButton
     */
    public void stateFollowButton(String _userID , final Button followButton){
        myRef.child("follower_list").child(_userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().toString().equals(userID)){
                        followButton.setText("Following");
                        followButton.setBackgroundResource(R.drawable.rounded_edit_text);
                        followButton.setTextColor(Color.BLACK);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Here we will add the Post object to firebase database and firebase storage.
     * @param post
     */
    public void addPostToFireBase(final Post post, Uri _uri){
        String username = getUserName();
        Integer uniquqeIdentity = randomUniqueIdentifier();
        post.setUniqueIdentifier(uniquqeIdentity);
        myRef.child("Posts").child(userID).child(Integer.toString(uniquqeIdentity)).setValue(post);
        myRef.child("random_search_post_list").child(Integer.toString(uniquqeIdentity)).setValue(post);

        //myRef.child("home_feed_post_list").child(userID).child(Integer.toString(uniquqeIdentity)).setValue(post);
        myRef.child("comment_section").child(userID).child(Integer.toString(uniquqeIdentity)).child(username).setValue(post.getCaption());
        StorageReference storageReference = mStorageRef.child("posts/users/" + userID + "/" + uniquqeIdentity + "/" );
        storageReference.putFile(_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                toastMessage("Post Upload Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage("Post Upload Failed");
            }
        });

        // Only do this the line of code below to its followers


    }

    public void run(){
        myRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                System.out.println(mutableData.getChildrenCount());
                long value = 0;
                if(mutableData.getValue() != null) {
                    String numQuestions = (String) mutableData.getValue();
                    value = Long.parseLong(numQuestions, 16);
                }
                value++;
                String incHex = Long.toHexString(value);
                mutableData.setValue(incHex);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
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
    public void uploadPictureFromCamera( Bitmap photoTaken){
        StorageReference storageReference = mStorageRef.child("profilePictures/users/" + userID + "/" );
        storageReference.putFile(getImageUri(myContext,photoTaken)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
    public void findProfilePicture(final ImageView selectedImage, final ImageView account_profile_picture, final Activity currentClass){
        mStorageRef.child("profilePictures/users/" + userID + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //System.out.println("This is my uri: " + uri);

                Glide.with(currentClass).load(uri).apply(RequestOptions.circleCropTransform()).into(selectedImage);
                if(account_profile_picture != null){
                    Glide.with(currentClass).load(uri).apply(RequestOptions.circleCropTransform()).into(account_profile_picture);
                }else {
                    System.out.println("This is the home activity page where there is no account profile image.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage("Download Failed");
            }
        });

    }
    public void search(final ImageView selectedImage, final ImageView account_profile_picture, final Activity currentClass,String _userID){
        mStorageRef.child("profilePictures/users/" + _userID + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //System.out.println("This is my uri: " + uri);

                Glide.with(currentClass).load(uri).apply(RequestOptions.circleCropTransform()).into(selectedImage);
                if(account_profile_picture != null){
                    Glide.with(currentClass).load(uri).apply(RequestOptions.circleCropTransform()).into(account_profile_picture);
                }else {
                    System.out.println("This is the home activity page where there is no account profile image.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage("Download Failed");
            }
        });

    }

    public void searchedProfilePicture(final ImageView bottomProfilePicture, final Activity currentClass){
        mStorageRef.child("profilePictures/users/" + userID + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(currentClass).load(uri).apply(RequestOptions.circleCropTransform()).into(bottomProfilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed to download Uri.");
            }
        });
    }

    public void searchProfilePicture(final ImageView selectedImage, final ImageView account_profile_picture, final Activity currentClass, String _userID){
        mStorageRef.child("profilePictures/users/" + _userID + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Glide.with(currentClass).load(uri).centerCrop().apply(RequestOptions.circleCropTransform()).into(account_profile_picture);
                Glide.with(currentClass).load(uri).centerCrop().apply(RequestOptions.circleCropTransform()).into(selectedImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void getSearchUsername(final TextView followerCount, final TextView followingCount, final TextView postCount, final TextView usernameTextView, final String _userID){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String current_follower_count = Long.toString(dataSnapshot.child("follower_list").child(_userID).getChildrenCount());
                String current_following_count = Long.toString(dataSnapshot.child("following_list").child(_userID).getChildrenCount());
                long current_post_count = dataSnapshot.child("Posts").child(_userID).getChildrenCount();
                String username = dataSnapshot.child("user_account_details").child(_userID).child("userName").getValue().toString();
                followerCount.setText(current_follower_count);
                followingCount.setText(current_following_count);
                postCount.setText(Long.toString(current_post_count));
                usernameTextView.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    // <------------------------- This is where all the EditProfile Activity methods will be accessed from ------------------------>

    /**
     * Updating the profile information in the EditProfile Activity. Method still not complete, method must handle empty edittexts.
     * @param name
     * @param username
     * @param email
     * @param bio
     * @param phone
     * @param gender
     */
    public void updateProfile(final EditText name, final EditText username, final EditText email, final EditText bio, final EditText phone, final EditText gender, Context context, ArrayList<String> changeStatus){
        if(!name.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()){
            changeStatus.add("true");
            myRef.child("user_account_details").child(userID).child("name").setValue(name.getText().toString().trim());
            myRef.child("user_account_details").child(userID).child("userName").setValue(username.getText().toString().trim());
            myRef.child("user_account_details").child(userID).child("profileDescription").setValue(bio.getText().toString().trim());
            myRef.child("user_account_details").child(userID).child("phone_number").setValue(phone.getText().toString().trim());
            myRef.child("user_account_details").child(userID).child("gender").setValue(gender.getText().toString().trim());
        }else{
            changeStatus.add("false");
            Toast.makeText(context, "Please fill in the required texts.", Toast.LENGTH_LONG).show();

        }


    }
    public void showProfileDetails(final EditText name, final EditText username, final EditText email, final EditText bio, final EditText phone, final EditText gender){
        myRef.child("user_account_details").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String _username = dataSnapshot.child("userName").getValue().toString();
                String _name = dataSnapshot.child("name").getValue().toString();
                String _email = getEmail();
                String _bio = dataSnapshot.child("profileDescription").getValue().toString();
                String _phone = dataSnapshot.child("phone_number").getValue().toString();
                String _gender = dataSnapshot.child("gender").getValue().toString();
                username.setText(_username);
                name.setText(_name);
                email.setText(_email);
                phone.setText(_phone);
                if(!_bio.isEmpty()){
                    bio.setText(_bio);
                }
                gender.setText(_gender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showPostInfor(final String _userID, final Integer id, final TextView topUsername, final TextView bottomUsername, final TextView commentText, final TextView likesText, final ImageView postPicture){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(id+ "                   kakakakakaka");
                System.out.println("<-------------------datasnapshot------------------------->"+dataSnapshot.child("user_account_details").child(_userID));
                System.out.println("<-------------------userID------------------------->"+_userID);
                String username = dataSnapshot.child("user_account_details").child(_userID).child("userName").getValue().toString();
                long likesInt = dataSnapshot.child("post_likes").child(Integer.toString(id)).getChildrenCount();
                topUsername.setText(username);
                bottomUsername.setText(username);
                String likes = Long.toString(likesInt);
                likesText.setText(likes);
                String comment = dataSnapshot.child("comment_section").child(_userID).child(Integer.toString(id)).child(username).getValue().toString();
                commentText.setText(comment);
                mStorageRef.child("posts/users/" + _userID + "/" + Integer.toString(id) + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(myContext.getApplicationContext()).load(uri).fitCenter().into(postPicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void clickListenersPostLayout(final ImageView heartButton, final Integer id){
        Drawable one = myContext.getDrawable(R.drawable.ic_red_heart);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String userID = mAuth.getCurrentUser().getUid();

                Followers follower = new Followers(userID);
                myRef.child("post_likes").child(id.toString()).child(userID).setValue(follower);
                heartButton.setImageResource(R.drawable.ic_red_heart);
                //Glide.with(myContext).load(R.drawable.ic_red_heart).into(viewHolder.heartButton);
                //viewHolder.heartButton.setImageDrawable();
                clickListenersPostLayout(heartButton,id);
            }
        });
        //



        myRef.child("post_likes").child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(userID)){
                        Glide.with(myContext).load(R.drawable.ic_red_heart).into(heartButton);

                        // User already likes this post, so this will now be the listener
                        heartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Here is the bug
                                myRef.child("post_likes").child(id.toString()).child(userID).removeValue();
                                Glide.with(myContext).load(R.drawable.ic_insta_heart).into(heartButton);
                                clickListenersPostLayout(heartButton,id);
                            }
                        });
                        break;
                    }

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
