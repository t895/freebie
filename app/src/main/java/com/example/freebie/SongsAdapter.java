package com.example.freebie;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freebie.models.Song;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private Context context;
    private List<Song> songs;

    public SongsAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
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
    public int getItemCount() {
        return songs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivImage;
        private TextView tvArtist;
        private TextView tvSongLength;
        private RelativeLayout btnSong;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSongTitle);
            ivImage = itemView.findViewById(R.id.ivAlbum);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvSongLength = itemView.findViewById(R.id.tvSongLength);
            btnSong = itemView.findViewById(R.id.btnSong);
        }

        public void bind(Song song) {
            tvTitle.setText(song.getTitle());
            tvArtist.setText(song.getArtist());
            tvSongLength.setText(song.getLength());

            btnSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: Create one mediaPlayer that changes between songs
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(song.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        songs.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Song> songs) {
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }
}