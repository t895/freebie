package com.t895.freebie;

import static com.t895.freebie.MainActivity.mainActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.t895.freebie.models.Song;
import com.sothree.slidinguppanel.PanelState;

public class MediaPlayerService {

    public static MediaPlayerService mediaPlayerService;
    private static Context context;

    public static MediaPlayer mediaPlayer;
    public static Song currentlyPlayingSong;

    private ImageView ivNowPlayingImage;
    private TextView tvNowPlayingSong;
    private Button btnPlay;

    public MediaPlayerService(Context context) {
        MediaPlayerService.context = context;
        mediaPlayer = new MediaPlayer();

        // Set currently playing song to nothing when song completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                currentlyPlayingSong = null;

                ivNowPlayingImage = mainActivity.findViewById(R.id.ivNowPlaying);
                tvNowPlayingSong = mainActivity.findViewById(R.id.tvNowPlayingSong);
                btnPlay = mainActivity.findViewById(R.id.btnPlay);

                Glide.with(context)
                        .load(R.drawable.ic_image_loading)
                        .into(ivNowPlayingImage);

                tvNowPlayingSong.setText(R.string.nothing_playing);
                btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);

                mainActivity.panelLayout.setPanelState(PanelState.HIDDEN);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                ivNowPlayingImage = mainActivity.findViewById(R.id.ivNowPlaying);
                tvNowPlayingSong = mainActivity.findViewById(R.id.tvNowPlayingSong);
                btnPlay = mainActivity.findViewById(R.id.btnPlay);

                Glide.with(context)
                        .load(currentlyPlayingSong.getUri())
                        .transform(new RoundedCorners(32))
                        .into(ivNowPlayingImage);

                tvNowPlayingSong.setText(currentlyPlayingSong.getTitle());
                btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);

                mainActivity.panelLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
    }

    public static MediaPlayerService getInstance(Context context) {
        MediaPlayerService.context = context;
        if(mediaPlayerService == null)
            mediaPlayerService = new MediaPlayerService(context);
        return mediaPlayerService;
    }
}
