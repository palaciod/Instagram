package com.example.instagram.Models;

import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class GridViewAdapter extends ArrayAdapter<Uri> {
    public GridViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
