package com.t895.freebie.activities

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.material.color.MaterialColors
import com.google.android.material.elevation.ElevationOverlayProvider
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
import com.t895.freebie.utils.ThemeHelper
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val ITEM_SELECTED = "item_selected"
    private val ITEM_KEY = "item"

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var artistsFragment: ArtistsFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !areSongsReady() }

        ThemeHelper.setTheme(this)

        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setInsets()

        if (songState.value == MediaInitialization.SongInitializationState.NOT_YET_INITIALIZED) {
            Thread {
                MediaInitialization.init(applicationContext)
            }.start()
        }

        homeFragment = HomeFragment()
        albumsFragment = AlbumsFragment()
        artistsFragment = ArtistsFragment()
        settingsFragment = SettingsFragment()
        mBinding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            val fragment: Fragment
            val fragmentTag: String
            when (item.itemId) {
                R.id.action_home -> {
                    fragment = homeFragment
                    fragmentTag = "HomeFragment"
                }

                R.id.action_albums -> {
                    fragment = albumsFragment
                    fragmentTag = "AlbumsFragment"
                }

                R.id.action_artists -> {
                    fragment = artistsFragment
                    fragmentTag = "ArtistsFragment"
                }

                R.id.action_settings -> {
                    fragment = settingsFragment
                    fragmentTag = "SettingsFragment"
                }

                else -> {
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

        // Restore previous selection
        val sharedPreferences: SharedPreferences = applicationContext
            .getSharedPreferences(ITEM_SELECTED, Context.MODE_PRIVATE)
        val previouslySelectedItem: Int = sharedPreferences.getInt(ITEM_KEY, 0)
        if (previouslySelectedItem != 0) {
            mBinding.bottomNavigation.selectedItemId = previouslySelectedItem
        } else {
            mBinding.bottomNavigation.selectedItemId = R.id.action_home
        }
    }

    override fun onStart() {
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

    override fun onStop() {
        super.onStop()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            { MediaController.releaseFuture(controllerFuture) },
            MoreExecutors.directExecutor()
        )
    }

    private fun setInsets() {
        // Don't fit system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Insets for app bar
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.appBarLayout) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val mlp = mBinding.appBarLayout.layoutParams as MarginLayoutParams
            mlp.topMargin = insets.top
            mBinding.appBarLayout.layoutParams = mlp

            // Wait until the BottomNavigationView is drawn on screen before getting the height
            mBinding.bottomNavigation.post {
                mBinding.flContainer.setPadding(
                    0,
                    0,
                    0,
                    mBinding.bottomNavigation.measuredHeight + insets.bottom
                )
            }

            @ColorInt val navigationBarColor: Int = ElevationOverlayProvider(this).compositeOverlay(
                MaterialColors.getColor(
                    window.decorView,
                    R.attr.colorSurface
                ), mBinding.bottomNavigation.elevation
            )
            ThemeHelper.setNavigationBarColor(this, navigationBarColor)
            windowInsets
        }
    }
}
