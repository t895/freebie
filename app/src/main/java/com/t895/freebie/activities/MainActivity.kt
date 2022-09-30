package com.t895.freebie.activities

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.t895.freebie.*
import com.t895.freebie.MediaInitialization.areSongsReady
import com.t895.freebie.MediaInitialization.songState
import com.t895.freebie.databinding.ActivityMainBinding
import com.t895.freebie.fragments.AlbumsFragment
import com.t895.freebie.fragments.ArtistsFragment
import com.t895.freebie.fragments.HomeFragment
import com.t895.freebie.fragments.SettingsFragment
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity()
{
    private val TAG = "MainActivity"

    private val ITEM_SELECTED = "item_selected"
    private val ITEM_KEY = "item"

    private lateinit var homeFragment: HomeFragment
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var artistsFragment: ArtistsFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?)
    {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !areSongsReady() }

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (songState.value == MediaInitialization.SongInitializationState.NOT_YET_INITIALIZED)
        {
            Thread {
                MediaInitialization.init(applicationContext)
            }.start()
        }

        homeFragment = HomeFragment()
        albumsFragment = AlbumsFragment()
        artistsFragment = ArtistsFragment()
        settingsFragment = SettingsFragment()
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            val fragment: Fragment
            val fragmentTag: String
            when (item.itemId)
            {
                R.id.action_home ->
                {
                    fragment = homeFragment
                    fragmentTag = "HomeFragment"
                }
                R.id.action_albums ->
                {
                    fragment = albumsFragment
                    fragmentTag = "AlbumsFragment"
                }
                R.id.action_artists ->
                {
                    fragment = artistsFragment
                    fragmentTag = "ArtistsFragment"
                }
                R.id.action_settings ->
                {
                    fragment = settingsFragment
                    fragmentTag = "SettingsFragment"
                }
                else ->
                {
                    fragment = homeFragment
                    fragmentTag = "HomeFragment"
                }
            }

            // Remember current selection
            val sharedPreferences: SharedPreferences = applicationContext
                .getSharedPreferences(ITEM_SELECTED, Context.MODE_PRIVATE)
            val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
            myEdit.putInt(ITEM_KEY, item.itemId)
            myEdit.apply()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.flContainer, fragment, fragmentTag).commit()
            true
        }

        // Wait until the BottomNavigationView is drawn on screen before getting the height
        binding.bottomNavigation.post {
            binding.flContainer.setPadding(0, 0, 0, binding.bottomNavigation.measuredHeight)
        }

        // Restore previous selection
        val sharedPreferences: SharedPreferences = applicationContext
            .getSharedPreferences(ITEM_SELECTED, Context.MODE_PRIVATE)
        val previouslySelectedItem: Int = sharedPreferences.getInt(ITEM_KEY, 0)
        if (previouslySelectedItem != 0)
        {
            binding.bottomNavigation.selectedItemId = previouslySelectedItem
        }
        else
        {
            binding.bottomNavigation.selectedItemId = R.id.action_home
        }
    }

    override fun onStart()
    {
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                controllerFuture.get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
        super.onStart()
    }

    override fun onStop()
    {
        super.onStop()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            { MediaController.releaseFuture(controllerFuture) },
            MoreExecutors.directExecutor()
        )
    }
}
