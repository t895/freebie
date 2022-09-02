package com.t895.freebie;

import android.media.MediaPlayer;

import com.t895.freebie.models.Song;

public class MediaPlayerHelper
{
  public static final String TAG = "MediaPlayerService";

  public static MediaPlayer mediaPlayer;
  public static Song currentlyPlayingSong;

  public MediaPlayerHelper(UIManager manager)
  {
    if (mediaPlayer == null)
      mediaPlayer = new MediaPlayer();

    mediaPlayer.setOnCompletionListener(mp -> manager.songFinished());
    mediaPlayer.setOnPreparedListener(mediaPlayer -> manager.setActiveSong());
  }

  public static void setCurrentlyPlayingSong(Song song)
  {
    currentlyPlayingSong = song;
  }

  public static Song getCurrentlyPlayingSong()
  {
    return currentlyPlayingSong;
  }
}
