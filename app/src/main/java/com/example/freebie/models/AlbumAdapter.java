package com.example.freebie.models;

import static com.example.freebie.MainActivity.currentlyPlayingSong;
import static com.example.freebie.MainActivity.mediaPlayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.freebie.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    private Context context;
    private List<Song> albumFiles;
    View view;

    public AlbumAdapter(Context context, List<Song> albumFiles){
        this.context = context;
        this.albumFiles = albumFiles;


    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyHolder holder, int position){
        Song album = albumFiles.get(position);
        holder.bind(album);

    }
    @Override
    public int getItemCount(){ return albumFiles.size(); }

    class MyHolder extends RecyclerView.ViewHolder{

        private ImageView albumImage;
        private TextView albumName;
        private RelativeLayout btnAlbum;

        public MyHolder (@NonNull View itemView){
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumImage);
            albumName = itemView.findViewById(R.id.albumName);
        }

        public void bind(Song album) {
            btnAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Reset the song that the media player is referencing
                        mediaPlayer.reset();

                        // Set the correct song path and start the player
                        mediaPlayer.setDataSource(album.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        // Track the currently playing song globally
                        currentlyPlayingSong = album;
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(32));

            Resources res = context.getResources();
            Drawable placeholderFigure = ResourcesCompat.getDrawable(res, android.R.drawable.ic_menu_gallery, null);

            Glide.with(context)
                    .load(album.getAlbumArt())
                    .apply(requestOptions)
                    .placeholder(placeholderFigure)
                    .error(placeholderFigure)
                    .into(albumImage);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        albumFiles.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Song> songs) {
        this.albumFiles.addAll(songs);
        notifyDataSetChanged();
    }
}


















































