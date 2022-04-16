package com.example.freebie.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freebie.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Song> albumFiles;
    View view;

    public AlbumAdapter(Context mContext, ArrayList<Song> albumFiles){
        this.context = context;
        this.albumFiles = albumFiles;


    }
    public class MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        view = LayoutInflater.from(context).inflate(R.layout.albumItem, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public int getItemCount(){
        return albumFiles.size();
    }
}
