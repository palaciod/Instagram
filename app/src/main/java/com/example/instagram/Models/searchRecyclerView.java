package com.example.instagram.Models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Activities.AccountProfile;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.instagram.Activities.searchedUserProfile;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class searchRecyclerView extends RecyclerView.Adapter<searchRecyclerView.ViewHolder>{
    private ArrayList<String> usernames = new ArrayList<>();
    private Context myContext;
    private ArrayList<String> userIDList = new ArrayList<>();

    public searchRecyclerView(Context _context, ArrayList<String> _usernames, ArrayList<String> _userIDList){
        myContext = _context;
        usernames = _usernames;
        userIDList = _userIDList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_bar_row_layout, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.username.setText(usernames.get(i));
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mFirebaseDatabase.getReference();
        final String userID = userIDList.get(i);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = mStorageRef.child("profilePictures/users/" + userID + "/" );
        System.out.println(userID);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("This is the user's uri: " + uri);
                Glide.with(myContext).load(uri).centerCrop().apply(RequestOptions.circleCropTransform()).into(viewHolder.profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        viewHolder.userProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserID = mAuth.getCurrentUser().getUid();
                // If the user searches himself up then itll lead him to his own account profile. If not then it'll go to the profile the user was searching for.
                if(currentUserID.equals(userID)){
                    Intent intent = new Intent(myContext, AccountProfile.class);
                    myContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(myContext, searchedUserProfile.class);
                    intent.putExtra("userID", userID);
                    myContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView profilePicture;
        LinearLayout userProfileLink;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            profilePicture = itemView.findViewById(R.id.profile_image);
            userProfileLink = itemView.findViewById(R.id.userProfileLink);
        }
    }
}
