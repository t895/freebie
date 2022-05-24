package com.t895.freebie.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.t895.freebie.MediaPlayerService;
import com.t895.freebie.models.Song;
import com.t895.freebie.R;
import com.t895.freebie.utils.RoundedCornerHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private static final String TAG = "SongsAdapter";

    private static final int CORNER_RADIUS_DP = 8;

    private Context context;
    public ArrayList<Song> songs;

    private RequestOptions requestOptions;
    private Drawable placeholderFigure;
    private MediaMetadataRetriever imageRetriever;

    public SongsAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;

        requestOptions = new RequestOptions();
        requestOptions.transform(new RoundedCorners(RoundedCornerHelper.dpToPx(context, CORNER_RADIUS_DP)));

        Resources res = context.getResources();
        placeholderFigure = ResourcesCompat.getDrawable(res, android.R.drawable.ic_menu_gallery, null);

        imageRetriever = new MediaMetadataRetriever();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() { return songs.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivAlbum;
        private TextView tvArtist;
        private TextView tvSongLength;
        private RelativeLayout btnSong;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSongTitle);
            ivAlbum = itemView.findViewById(R.id.ivAlbum);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvSongLength = itemView.findViewById(R.id.tvSongLength);
            btnSong = itemView.findViewById(R.id.btnSong);
        }

        public void bind(Song song) {
            tvTitle.setText(song.getTitle());
            tvArtist.setText(song.getArtist());
            tvSongLength.setText(song.getLength());

            btnSong.setOnClickListener(view ->
            {
                try {
                    // Reset the song that the media player is referencing
                    MediaPlayerService.mediaPlayer.reset();

                    // Set the correct song path without uri identifier and start the player
                    MediaPlayerService.mediaPlayer.setDataSource(song.getUri().substring(5));
                    MediaPlayerService.mediaPlayer.prepare();
                    MediaPlayerService.mediaPlayer.start();

                    // Track the currently playing song globally
                    MediaPlayerService.currentlyPlayingSong = song;
                } catch (Exception e) { e.printStackTrace(); }
            });

            Glide.with(context)
                    .load(song.getUri())
                    .apply(requestOptions)
                    .placeholder(placeholderFigure)
                    .error(placeholderFigure)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivAlbum);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        this.songs.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Song> songs) {
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public void add(Song song) { this.songs.add(song); }
}