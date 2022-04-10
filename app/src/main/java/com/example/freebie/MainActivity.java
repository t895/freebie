package com.example.freebie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.freebie.fragments.AlbumsFragment;
import com.example.freebie.fragments.ArtistsFragment;
import com.example.freebie.fragments.HomeFragment;
import com.example.freebie.fragments.SettingsFragment;
import com.example.freebie.models.Song;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();

    public static MediaPlayer mediaPlayer;
    public static Song currentlyPlayingSong;

    public HomeFragment homeFragment;
    public AlbumsFragment albumsFragment;
    public ArtistsFragment artistsFragment;
    public SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Create each fragment in advance
        if(savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            albumsFragment = AlbumsFragment.newInstance();
            artistsFragment = ArtistsFragment.newInstance();
            settingsFragment = SettingsFragment.newInstance();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        displayHomeFragment();
                        break;
                    case R.id.action_albums:
                        displayAlbumsFragment();
                        break;
                    case R.id.action_artists:
                        displayArtistsFragment();
                        break;
                    case R.id.action_settings:
                        displaySettingsFragment();
                        break;
                    default:
                        displayHomeFragment();
                        break;
                }
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        // Set currently playing song to nothing when song completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) { currentlyPlayingSong = null; }
        });
    }

    protected void displayHomeFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (homeFragment.isAdded()) { // if the fragment is already in container
            ft.show(homeFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, homeFragment, "HomeFragment");
        }
        // Hide non-relevant fragments
        if (albumsFragment.isAdded()) { ft.hide(albumsFragment); }
        if (artistsFragment.isAdded()) { ft.hide(artistsFragment); }
        if (settingsFragment.isAdded()) { ft.hide(settingsFragment); }
        // Commit changes
        ft.commit();
    }

    protected void displayAlbumsFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (albumsFragment.isAdded()) { // if the fragment is already in container
            ft.show(albumsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, albumsFragment, "AlbumsFragment");
        }
        // Hide non-relevant fragments
        if (homeFragment.isAdded()) { ft.hide(homeFragment); }
        if (artistsFragment.isAdded()) { ft.hide(artistsFragment); }
        if (settingsFragment.isAdded()) { ft.hide(settingsFragment); }
        // Commit changes
        ft.commit();
    }

    protected void displayArtistsFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (artistsFragment.isAdded()) { // if the fragment is already in container
            ft.show(artistsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, artistsFragment, "ArtistsFragment");
        }
        // Hide non-relevant fragments
        if (homeFragment.isAdded()) { ft.hide(homeFragment); }
        if (albumsFragment.isAdded()) { ft.hide(albumsFragment); }
        if (settingsFragment.isAdded()) { ft.hide(settingsFragment); }
        // Commit changes
        ft.commit();
    }

    protected void displaySettingsFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (settingsFragment.isAdded()) { // if the fragment is already in container
            ft.show(settingsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, settingsFragment, "settingsFragment");
        }
        // Hide non-relevant fragments
        if (homeFragment.isAdded()) { ft.hide(homeFragment); }
        if (albumsFragment.isAdded()) { ft.hide(albumsFragment); }
        if (artistsFragment.isAdded()) { ft.hide(artistsFragment); }
        // Commit changes
        ft.commit();
    }
}