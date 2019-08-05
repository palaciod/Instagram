package com.example.instagram.Models;

import android.app.Activity;
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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.instagram.Activities.PostLayout;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private postCountListener mListener;
    private ArrayList<ArrayList<Integer>> mySelectedImageUris = new ArrayList<>();
    private Context myContext;
    private int position;
    private String userID;
    private ArrayList<ArrayList<String>> userIDList;
    public RecyclerViewAdapter(ArrayList<ArrayList<Integer>> _mySelectedImageUris, Context _context, String _userID, ArrayList<ArrayList<String>> list){
        mySelectedImageUris = _mySelectedImageUris;
        myContext = _context;
        position = 0;
        userID = _userID;
        userIDList = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_post_row_layout, viewGroup,false);
        ViewHolder vHolder = new ViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "OnBindViewHolder: called.");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mFirebaseDatabase.getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = mStorageRef.child("posts/users/" );
//        try{
//
//
//        }catch (IndexOutOfBoundsException e){
//            System.out.println("FIrst image not here");
//        }
        if(mySelectedImageUris.get(i).get(0)!=null){
            if(!userIDList.isEmpty()){
                userID = userIDList.get(i).get(0);
            }
            System.out.println("<--------------------------------------------->");
            System.out.println(mySelectedImageUris.get(i).get(0));
            //Glide.with(myContext).load(mySelectedImageUris.get(i).get(0)).centerCrop().into(viewHolder.firstImage);
            storageReference.child(userID).child(mySelectedImageUris.get(i).get(0).toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(myContext).load(uri).centerCrop().into(viewHolder.firstImage);
                    viewHolder.firstImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(myContext, PostLayout.class);
                            intent.putExtra("UserID",userID);
                            intent.putExtra("identifier", mySelectedImageUris.get(i).get(0).toString());
                            myContext.startActivity(intent);
                            ((Activity) myContext).finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "First Recycler Load Failed");
                }
            });
        }

        try{
            if(mySelectedImageUris.get(i).get(1)!=null){
                if(!userIDList.isEmpty()){
                    userID = userIDList.get(i).get(1);
                }
                System.out.println("<--------------------------------------------->");
                System.out.println(mySelectedImageUris.get(i).get(1));
                storageReference.child(userID).child(mySelectedImageUris.get(i).get(1).toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(myContext).load(uri).centerCrop().into(viewHolder.secondImage);
                        viewHolder.secondImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(myContext, PostLayout.class);
                                intent.putExtra("UserID",userID);
                                intent.putExtra("identifier", mySelectedImageUris.get(i).get(1).toString());
                                myContext.startActivity(intent);
                                ((Activity) myContext).finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Second Recycler Load Failed");
                    }
                });
            }

        }catch (IndexOutOfBoundsException e){
            System.out.println("There is no second picture.");
        }

        try {
            if(mySelectedImageUris.get(i).get(2)!=null){
                if(!userIDList.isEmpty()){
                     userID = userIDList.get(i).get(2);
                }
                System.out.println("<--------------------------------------------->");
                System.out.println(mySelectedImageUris.get(i).get(2));
                storageReference.child(userID).child(mySelectedImageUris.get(i).get(2).toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(myContext).load(uri).centerCrop().into(viewHolder.thirdImage);
                        viewHolder.thirdImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(myContext, PostLayout.class);
                                intent.putExtra("UserID",userID);
                                intent.putExtra("identifier", mySelectedImageUris.get(i).get(2).toString());
                                myContext.startActivity(intent);
                                ((Activity) myContext).finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Third Recycler Load Failed");
                    }
                });
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("No third image");
        }


    }

    @Override
    public int getItemCount() {
        return mySelectedImageUris.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView firstImage;
        ImageView secondImage;
        ImageView thirdImage;
        RelativeLayout row;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstImage = itemView.findViewById(R.id.first_image);
            secondImage = itemView.findViewById(R.id.second_image);
            thirdImage = itemView.findViewById(R.id.third_image);
            row = itemView.findViewById(R.id.row);
        }
    }

    /**
     * Need an interface to use a callback function to get the post count from the textView. -> Better than reading from the databse every time.
     */
    public interface postCountListener{
        int getPostCount();
    }
}
