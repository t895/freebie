package com.t895.freebie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.t895.freebie.fragments.AlbumsFragment;
import com.t895.freebie.fragments.ArtistsFragment;
import com.t895.freebie.fragments.HomeFragment;
import com.t895.freebie.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.PanelState;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity
{

  public static final String TAG = "MainActivity";

  public static final String ITEM_SELECTED = "item_selected";
  public static final String ITEM_KEY = "item";

  public static MainActivity mainActivity;

  public HomeFragment homeFragment;
  public AlbumsFragment albumsFragment;
  public ArtistsFragment artistsFragment;
  public SettingsFragment settingsFragment;

  public SlidingUpPanelLayout panelLayout;
  public Button btnPlay;
  private ProgressBar progressBar;

  private static boolean gettingSongs = false;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mainActivity = this;

    panelLayout = findViewById(R.id.sliding_layout);
    btnPlay = findViewById(R.id.btnPlay);
    progressBar = findViewById(R.id.progressBar);

    if (!gettingSongs)
    {
      Thread GettingSongsFromDisk = new Thread(() ->
      {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
        gettingSongs = true;
        SongRetrievalService.getAllSongs();
        gettingSongs = false;
        runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
      });
      GettingSongsFromDisk.start();
    }


    MediaPlayerService mediaPlayerService = new MediaPlayerService();
    if (MediaPlayerService.currentlyPlayingSong == null)
      panelLayout.setPanelState(PanelState.HIDDEN);
    else
      mediaPlayerService.setActiveSong();

    btnPlay.setOnClickListener(view ->
    {
      if (MediaPlayerService.mediaPlayer.isPlaying())
      {
        MediaPlayerService.mediaPlayer.pause();
        btnPlay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play,
                0, 0, 0);
      }
      else
      {
        MediaPlayerService.mediaPlayer.start();
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
}