package com.example.freebie;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.freebie.models.Song;

public class MediaPlayerService {

    public static MediaPlayerService mediaPlayerService;
    private static Context context;

    public static MediaPlayer mediaPlayer;
    public static Song currentlyPlayingSong;

    public MediaPlayerService(Context context) {
        MediaPlayerService.context = context;
        mediaPlayer = new MediaPlayer();

        // Set currently playing song to nothing when song completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) { currentlyPlayingSong = null; }
        });
    }

    public static MediaPlayerService getInstance(Context context) {
        MediaPlayerService.context = context;
        if(mediaPlayerService == null)
            mediaPlayerService = new MediaPlayerService(context);
        return mediaPlayerService;
    }
}
