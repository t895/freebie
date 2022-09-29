package com.t895.freebie

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.t895.freebie.models.Song

class MediaPlayerHelper
{
    private val TAG = "MediaPlayerHelper"

    companion object
    {
        var player: MediaController? = null
        private var currentlyPlayingSong: Song? = null

        fun playSong(song: Song)
        {
            player!!.removeMediaItems(0, 1)
            currentlyPlayingSong = song
            val mediaItem = MediaItem.Builder()
                .setUri(currentlyPlayingSong!!.uri.substring(5))
                .setMediaId(currentlyPlayingSong!!.title)
                .setTag(currentlyPlayingSong!!.title)
                .build()
            player!!.addMediaItem(mediaItem)
            player!!.play()
        }
    }

}
