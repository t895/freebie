package com.t895.freebie

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.t895.freebie.models.Album
import com.t895.freebie.models.Artist
import com.t895.freebie.models.Song
import java.io.IOException

object MediaInitialization {
    const val TAG = "SongRetrievalService"

    private const val BASE_URL = "https://api.discogs.com/database/"
    private const val API_KEY = "DdqsWWJtlNwZacBISHSNstrYdnqmZWqtLMikewEo"
    private const val SONGS_LOADED = "songs_loaded"
    private const val SIZE_KEY = "size"

    var songState: MutableLiveData<SongInitializationState> =
        MutableLiveData<SongInitializationState>(SongInitializationState.NOT_YET_INITIALIZED)

    private lateinit var queue: RequestQueue

    enum class SongInitializationState {
        NOT_YET_INITIALIZED,
        INITIALIZED
    }

    fun init(context: Context) {
        // Check if previously loaded songs are still in memory
        var sharedPreferences = context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE)
        val previousListSize = sharedPreferences.getInt(SIZE_KEY, 0)
        if (previousListSize == Song.list.size && Song.list.size != 0) {
            songState.postValue(SongInitializationState.INITIALIZED)
            return
        }
        songState.postValue(SongInitializationState.NOT_YET_INITIALIZED)

        queue = Volley.newRequestQueue(context)

        // Remove stale data
        Song.list.clear()
        Album.list.clear()
        Artist.list.clear()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )
        val mediaMetadataRetriever = MediaMetadataRetriever()

        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        ).use { songCursor ->
            if (songCursor!!.moveToFirst()) {
                val songTitleColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val songArtistColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val songAlbumColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val songPathColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val songDurationColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                var filePathUri: Uri

                do {
                    // Retrieve song path
                    filePathUri = Uri.parse(songCursor.getString(songPathColumn))

                    // Retrieve title, artist, and album
                    val titleString = songCursor.getString(songTitleColumn)
                    val artistString = songCursor.getString(songArtistColumn)
                    val albumString = songCursor.getString(songAlbumColumn)
                    val durationString = songCursor.getString(songDurationColumn)

                    // Retrieve song length
                    val rawLength = durationString.toLong()
                    val seconds = (rawLength % 60000 / 1000).toString()
                    val minutes = (rawLength / 60000).toString()
                    val length: String =
                        if (seconds.length == 1) "$minutes:0$seconds" else "$minutes:$seconds"

                    val song =
                        Song(titleString, artistString, albumString, length, "song:$filePathUri")
                    Song.list[song.title.hashCode()] = song
                } while (songCursor.moveToNext())
            }
        }

        // Create album/artist objects based on what songs were found
        for (song in Song.list.values) {
            if (!Album.list.containsKey(song.album.hashCode())) {
                Album.list[song.album.hashCode()] = Album(song.album, song.artist, song.uri)
            }

            if (!Artist.list.containsKey(song.artist.hashCode())) {
                Artist.list[song.artist.hashCode()] = Artist(song.artist, "")
            }
        }

        fetchArtistImages()

        try {
            mediaMetadataRetriever.release()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        sharedPreferences = context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putInt(SIZE_KEY, Song.list.size)
        myEdit.apply()
        songState.postValue(SongInitializationState.INITIALIZED)
    }

    private fun fetchArtistImages() {
        for (artist in Artist.list.values) {
            // Put together elements of request URL
            val artistNameURL = artist.name.replace(" ".toRegex(), "%20")
            val requestURL =
                (BASE_URL + "search?q=" + artistNameURL + "&per_page=1" + "&token=" + API_KEY)
            val jsonRequest = JsonObjectRequest(
                Request.Method.GET, requestURL, null,
                {
                    val results = it.getJSONArray("results")
                    val artistData = results.getJSONObject(0)
                    val artistPicture = artistData.getString("cover_image")
                    if (artistPicture.contains(".gif")) {
                        artist.profilePicture = ""
                    } else if (artistPicture != "") {
                        artist.profilePicture = artistPicture
                    }
                },
                {
                    it.printStackTrace()
                    artist.profilePicture = ""
                })
            jsonRequest.retryPolicy =
                DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            queue.add(jsonRequest)
        }
    }

    fun areSongsReady(): Boolean {
        return songState.value == SongInitializationState.INITIALIZED
    }
}
