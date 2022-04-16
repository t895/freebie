package com.example.freebie.models;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Song> albumFiles;

    public AlbumAdapter(Context mContext, ArrayList<Song> albumFiles){
        this.context = mContext;
        this.albumFiles = albumFiles;

    }
    public class MyViewHolder {

    }
}
