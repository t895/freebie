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
import com.t895.freebie.utils.RoundedCornerHelper;

public class MediaPlayerService
{

  public static final String TAG = "MediaPlayerService";

  private static final int CORNER_RADIUS_DP = 8;

  public static MediaPlayer mediaPlayer;
  public static Song currentlyPlayingSong;

  private ImageView ivNowPlayingImage;
  private TextView tvNowPlayingSong;
  private Button btnPlay;

  public MediaPlayerService()
  {
    if (mediaPlayer == null)
      mediaPlayer = new MediaPlayer();

    // Set currently playing song to nothing when song completes
    mediaPlayer.setOnCompletionListener(mp ->
    {
      currentlyPlayingSong = null;

      ivNowPlayingImage = mainActivity.findViewById(R.id.ivNowPlaying);
      tvNowPlayingSong = mainActivity.findViewById(R.id.tvNowPlayingSong);
      btnPlay = mainActivity.findViewById(R.id.btnPlay);

      tvNowPlayingSong.setText(R.string.nothing_playing);
      btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);

      mainActivity.panelLayout.setPanelState(PanelState.HIDDEN);
    });

    mediaPlayer.setOnPreparedListener(mediaPlayer -> setActiveSong());
  }

  public void setActiveSong()
  {
    ivNowPlayingImage = mainActivity.findViewById(R.id.ivNowPlaying);
    tvNowPlayingSong = mainActivity.findViewById(R.id.tvNowPlayingSong);
    btnPlay = mainActivity.findViewById(R.id.btnPlay);

    Glide.with(mainActivity.getApplicationContext())
            .load(currentlyPlayingSong.getUri())
            .transform(new RoundedCorners(RoundedCornerHelper
                    .dpToPx(mainActivity.getApplicationContext(),
                            CORNER_RADIUS_DP)))
            .into(ivNowPlayingImage);

    tvNowPlayingSong.setText(currentlyPlayingSong.getTitle());
    btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pause,
            0, 0, 0);

    mainActivity.panelLayout.setPanelState(PanelState.COLLAPSED);
  }
}
