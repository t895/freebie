package com.t895.freebie

import android.util.Log
import androidx.core.app.ComponentActivity
import androidx.lifecycle.Observer

class AfterSongInitializationRunner
{
    private val TAG: String = "AfterSongInitializationRunner"

    private lateinit var mObserver: Observer<MediaInitialization.SongInitializationState>

    fun runWithLifecycle(activity: ComponentActivity?, runnable: Runnable)
    {
        if (MediaInitialization.areSongsReady())
        {
            runnable.run()
        }
        else
        {
            mObserver = createObserver(runnable)
            if (activity != null)
            {
                Log.e(TAG, "Huh")
                MediaInitialization.songState.observe(activity, mObserver)
            }
        }
    }

    private fun createObserver(runnable: Runnable): Observer<MediaInitialization.SongInitializationState>
    {
        return Observer { state: MediaInitialization.SongInitializationState ->
            if (state == MediaInitialization.SongInitializationState.INITIALIZED)
            {
                cancel()
                runnable.run()
            }
        }
    }

    private fun cancel()
    {
        MediaInitialization.songState.removeObserver(mObserver)
    }
}