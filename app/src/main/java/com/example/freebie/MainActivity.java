package com.example.freebie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.freebie.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        Toast.makeText(MainActivity.this, "home pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_albums:
                        fragment = new HomeFragment();
//                        fragment = new ComposeFragment();
                        Toast.makeText(MainActivity.this, "albums pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_artists:
                        fragment = new HomeFragment();
//                        fragment = new ComposeFragment();
                        Toast.makeText(MainActivity.this, "artists pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                    default:
                        fragment = new HomeFragment();
//                        fragment = new ProfileFragment();
                        Toast.makeText(MainActivity.this, "settings pressed", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}