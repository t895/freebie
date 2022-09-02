package com.t895.freebie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.t895.freebie.MediaPlayerHelper;
import com.t895.freebie.R;
import com.t895.freebie.MediaInitialization;
import com.t895.freebie.UIManager;
import com.t895.freebie.fragments.AlbumsFragment;
import com.t895.freebie.fragments.ArtistsFragment;
import com.t895.freebie.fragments.HomeFragment;
import com.t895.freebie.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.PanelState;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.t895.freebie.utils.RoundedCornerHelper;

public class MainActivity extends AppCompatActivity implements UIManager
{
  public static final String TAG = "MainActivity";

  public static final String ITEM_SELECTED = "item_selected";
  public static final String ITEM_KEY = "item";

  private HomeFragment homeFragment;
  private AlbumsFragment albumsFragment;
  private ArtistsFragment artistsFragment;
  private SettingsFragment settingsFragment;

  private SlidingUpPanelLayout panelLayout;
  private Button btnPlay;
  private ImageView ivNowPlayingImage;
  private TextView tvNowPlayingSong;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
    splashScreen.setKeepOnScreenCondition(() -> MediaInitialization.loadingSongs);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    panelLayout = findViewById(R.id.sliding_layout);
    btnPlay = findViewById(R.id.btnPlay);
    ivNowPlayingImage = findViewById(R.id.ivNowPlaying);
    tvNowPlayingSong = findViewById(R.id.tvNowPlayingSong);

    if (!MediaInitialization.loadingSongs)
    {
      new Thread(() -> MediaInitialization.getAllSongs(getApplicationContext())).start();
    }

    new MediaPlayerHelper(this);
    if (MediaPlayerHelper.currentlyPlayingSong == null)
      panelLayout.setPanelState(PanelState.HIDDEN);
    else
      setActiveSong();

    btnPlay.setOnClickListener(view ->
    {
      if (MediaPlayerHelper.mediaPlayer.isPlaying())
      {
        MediaPlayerHelper.mediaPlayer.pause();
        btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play,
                0, 0, 0);
      }
      else
      {
        MediaPlayerHelper.mediaPlayer.start();
        btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pause,
                0, 0, 0);
      }
    });

    // Create each fragment in advance
    homeFragment = new HomeFragment();
    albumsFragment = new AlbumsFragment();
    artistsFragment = new ArtistsFragment();
    settingsFragment = new SettingsFragment();

    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
    bottomNavigationView.setOnItemSelectedListener(item ->
    {
      Fragment fragment = null;
      String fragmentTag = null;
      switch (item.getItemId())
      {
        case R.id.action_home:
          fragment = homeFragment;
          fragmentTag = "HomeFragment";
          break;
        case R.id.action_albums:
          fragment = albumsFragment;
          fragmentTag = "AlbumsFragment";
          break;
        case R.id.action_artists:
          fragment = artistsFragment;
          fragmentTag = "ArtistsFragment";
          break;
        case R.id.action_settings:
          fragment = settingsFragment;
          fragmentTag = "SettingsFragment";
          break;
        default:
          fragment = homeFragment;
          fragmentTag = "HomeFragment";
          break;
      }

      // Remember current selection
      SharedPreferences sharedPreferences = getApplicationContext()
              .getSharedPreferences(ITEM_SELECTED, Context.MODE_PRIVATE);
      SharedPreferences.Editor myEdit = sharedPreferences.edit();
      myEdit.putInt(ITEM_KEY, item.getItemId());
      myEdit.apply();

      getSupportFragmentManager().beginTransaction()
              .setCustomAnimations(
                      R.anim.fade_in,
                      R.anim.fade_out
              )
              .replace(R.id.flContainer, fragment, fragmentTag).commit();
      return true;
    });

    // Restore previous selection
    SharedPreferences sharedPreferences = getApplicationContext()
            .getSharedPreferences(ITEM_SELECTED, Context.MODE_PRIVATE);
    int previouslySelectedItem = sharedPreferences.getInt(ITEM_KEY, 0);
    if (previouslySelectedItem != 0)
      bottomNavigationView.setSelectedItemId(previouslySelectedItem);
    else
      bottomNavigationView.setSelectedItemId(R.id.action_home);
  }

  // UI Manager
  @Override
  public void songFinished()
  {
    MediaPlayerHelper.setCurrentlyPlayingSong(null);
    tvNowPlayingSong.setText(R.string.nothing_playing);
    btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
    panelLayout.setPanelState(PanelState.HIDDEN);
  }

  @Override
  public void setActiveSong()
  {
    Glide.with(getApplicationContext())
            .load(MediaPlayerHelper.getCurrentlyPlayingSong().getUri())
            .transform(new RoundedCorners(RoundedCornerHelper.dpToPx(getApplicationContext(),
                    RoundedCornerHelper.EIGHT_DP)))
            .into(ivNowPlayingImage);

    tvNowPlayingSong.setText(MediaPlayerHelper.getCurrentlyPlayingSong().getTitle());
    btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);

    panelLayout.setPanelState(PanelState.COLLAPSED);
  }
}