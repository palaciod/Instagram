package com.example.instagram.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Activities.HomeFeedActivity;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFeedRecyclerViewAdapter extends RecyclerView.Adapter<HomeFeedRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "HomeFeedRecycler";
    private HashMap<Integer, String> userIDList;
    private Context myContext;

    public HomeFeedRecyclerViewAdapter(Context _context, HashMap<Integer, String> _list){
        userIDList = _list;

        myContext = _context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_feed_recycler_post_layout, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.layout.setId(i);
        viewHolder.convoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("I have been pressed!!");
            }
        });
        final Object key = userIDList.keySet().toArray()[i];
        System.out.println("This is my key: " + key.toString());
        System.out.println("This is my value: " + userIDList.get(key));
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mFirebaseDatabase.getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = mStorageRef.child("posts/users/" );
        final String userID = mAuth.getCurrentUser().getUid();
        // Default listener


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("user_account_details").child(userIDList.get(key)).child("userName").getValue().toString();
                viewHolder.topUsername.setText(username);
                viewHolder.bottomUsername.setText(username);
                String comment = dataSnapshot.child("comment_section").child(userIDList.get(key)).child(key.toString()).child(username).getValue().toString();
                viewHolder.comment.setText(comment);
                String numberOfLikes = Long.toString(dataSnapshot.child("post_likes").child(key.toString()).getChildrenCount());
                System.out.print(dataSnapshot.child("post_likes").child(key.toString()).getKey() + " --------------------------------------------------------NIG");
                viewHolder.numberOfLikes.setText(numberOfLikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        storageReference.child(userIDList.get(key)).child(key.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Glide.with(myContext).load(uri).fitCenter().into(viewHolder.postImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Second Recycler Load Failed");
            }
        });
        mStorageRef.child("profilePictures").child("users").child(userIDList.get(key)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(myContext).load(uri).apply(RequestOptions.circleCropTransform()).into(viewHolder.profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Could Not Find Profile Picture");
            }
        });

//        viewHolder.heartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.heartButton.setImageResource(R.drawable.ic_red_heart);
//                viewHolder.numberOfLikes.setText("3");
//            }
//        });
        viewHolder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String userID = mAuth.getCurrentUser().getUid();

                Followers follower = new Followers(userID);
                myRef.child("post_likes").child(key.toString()).child(userID).setValue(follower);
                viewHolder.heartButton.setImageResource(R.drawable.ic_red_heart);
                //Glide.with(myContext).load(R.drawable.ic_red_heart).into(viewHolder.heartButton);
                //viewHolder.heartButton.setImageDrawable();
            }
        });
        //


        myRef.child("post_likes").child(key.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(userID)){
                        //Glide.with(myContext).load(R.drawable.ic_red_heart).into(viewHolder.heartButton);
                        viewHolder.heartButton.setImageResource(R.drawable.ic_red_heart);
                        // User already likes this post, so this will now be the listener
                        viewHolder.heartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Here is the bug
                                myRef.child("post_likes").child(key.toString()).child(userID).removeValue();
                                //Glide.with(myContext).load(R.drawable.ic_insta_heart).into(viewHolder.heartButton);
                                viewHolder.heartButton.setImageResource(R.drawable.ic_insta_heart);
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

    @Override
    public int getItemCount() {
        return userIDList.size();
    }

//
//    @Override
//    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        return false;
//    }
//
//    @Override
//    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        System.out.println(recyclerView.getVerticalScrollbarPosition() + " ----This is my scrollbar position yayayayayayayaya");
//        recyclerView.scrollToPosition(recyclerView.getVerticalScrollbarPosition());
//    }
//
//    @Override
//    public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;//
        TextView topUsername;//
        TextView bottomUsername;//
        TextView comment;
        ImageView profilePicture;//
        ImageView heartButton;
        ImageView convoButton;
        TextView numberOfLikes;
        TextView likes;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.something);
            postImage = itemView.findViewById(R.id.post_image);
            topUsername = itemView.findViewById(R.id.top_username);
            bottomUsername = itemView.findViewById(R.id.bottom_username);
            comment = itemView.findViewById(R.id.comment);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            heartButton = itemView.findViewById(R.id.like_button);
            convoButton = itemView.findViewById(R.id.conversation_button);
            numberOfLikes = itemView.findViewById(R.id.number_of_likes);
            likes = itemView.findViewById(R.id.likes);
        }
    }

}
