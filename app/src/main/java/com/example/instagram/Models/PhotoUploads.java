package com.example.instagram.Models;

import android.Manifest;

public class PhotoUploads {

    private void verify_permissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    }
}
