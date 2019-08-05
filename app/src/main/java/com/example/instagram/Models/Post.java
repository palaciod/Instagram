package com.example.instagram.Models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.EditText;

import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class Post {
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;
    private String caption;
    private Integer likes;
    private Integer uniqueIdentifier;
    private String userID;
    private String time;


    public Post ( String _caption, Integer _likes, Integer _uniqueIdentifier, String _userID, String _time){
       // selectedImageUri = _uri;
        caption = _caption;
        likes = _likes;
        uniqueIdentifier= _uniqueIdentifier;
        userID = _userID;
        time = _time;
    }

    /**
     * Used for Bitmaps.
     * @param _bitmap
     * @param _caption
     * @param _likes
     * @param _comments
     */
    public Post(Bitmap _bitmap, String _caption, Integer _likes,HashMap<String,String> _comments){
        selectedImageBitmap = _bitmap;
        caption = _caption;
        likes = _likes;

    }

    public String getCaption() {
        return caption;
    }

    public Uri getSelectedImageUri() {
        return selectedImageUri;
    }

    public Bitmap getSelectedImageBitmap() {
        return selectedImageBitmap;
    }

    public void setCaption(String _caption) {
        this.caption = _caption;
    }

    public void setSelectedImageBitmap(Bitmap selectedImageBitmap) {
        this.selectedImageBitmap = selectedImageBitmap;
    }

    public void setSelectedImageUri(Uri selectedImageUri) {
        this.selectedImageUri = selectedImageUri;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public String getUserID() {
        return userID;
    }

    public void setUniqueIdentifier(Integer uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
