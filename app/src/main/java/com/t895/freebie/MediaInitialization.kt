package com.t895.freebie

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.t895.freebie.models.Album
import com.t895.freebie.models.Artist
import com.t895.freebie.models.Song
import okhttp3.Headers
import org.json.JSONException
import java.io.IOException

object MediaInitialization
{
    const val TAG = "SongRetrievalService"

    private const val BASE_URL = "https://api.discogs.com/database/"
    private const val API_KEY = "DHqvPIxOJwmIMQHtHkkoewRydAnSCRwFXnNAOUCI"
    private const val SONGS_LOADED = "songs_loaded"
    private const val SIZE_KEY = "size"

    var songState: MutableLiveData<SongInitializationState> = MutableLiveData<SongInitializationState>(SongInitializationState.NOT_YET_INITIALIZED)

    enum class SongInitializationState
    {
        NOT_YET_INITIALIZED,
        INITIALIZED
    }

    fun init(context: Context)
    {
        Log.e(TAG, "This should be run once per onCreate")

        // Check if previously loaded songs are still in memory
        var sharedPreferences = context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE)
        val previousListSize = sharedPreferences.getInt(SIZE_KEY, 0)
        if (previousListSize == Song.songArrayList.size && Song.songArrayList.size != 0)
        {
            return
        }
        //songState.postValue(SongInitializationState.NOT_YET_INITIALIZED)

        // Remove stale data
        Song.songArrayList.clear()
        Album.albumArrayList.clear()
        Artist.artistArrayList.clear()

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
            if (songCursor!!.moveToFirst())
            {
                val songTitleColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val songArtistColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val songAlbumColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val songPathColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val songDurationColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                var filePathUri: Uri

                do
                {
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

                    // Find unique artists to add into artist collection
                    if (Artist.artistArrayList.size == 0)
                    {
                        getArtistDetails(artistString)
                    }
                    else
                    {
                        for (i in Artist.artistArrayList.indices)
                        {
                            if (Artist.artistArrayList[i].name == artistString)
                            {
                                break
                            }

                            if (i == Artist.artistArrayList.size - 1)
                            {
                                getArtistDetails(artistString)
                            }
                        }
                    }

                    // Find unique albums to add into album collection and decode relevant album art
                    if (Album.albumArrayList.size == 0)
                    {
                        val albumItem = Album(albumString, artistString, "song:$filePathUri")
                        Album.albumArrayList.add(albumItem)
                    }
                    else
                    {
                        for (i in Album.albumArrayList.indices)
                        {
                            if (Album.albumArrayList[i].title == albumString)
                            {
                                break
                            }

                            if (i == Album.albumArrayList.size - 1)
                            {
                                val albumItem =
                                    Album(albumString, artistString, "song:$filePathUri")
                                Album.albumArrayList.add(albumItem)
                            }
                        }
                    }
                    val song =
                        Song(titleString, artistString, albumString, length, "song:$filePathUri")
                    Song.songArrayList.add(song)
                    Log.e(TAG, "Added song - " + song.title)
                } while (songCursor.moveToNext())
            }
        }

        try
        {
            mediaMetadataRetriever.release()
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

        sharedPreferences = context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putInt(SIZE_KEY, Song.songArrayList.size)
        myEdit.apply()
        songState.postValue(SongInitializationState.INITIALIZED)
    }

    private fun getArtistDetails(artistString: String)
    {
        // Put together elements of request URL
        val artistNameURL = artistString.replace(" ".toRegex(), "%20")
        val requestURL = (BASE_URL + "search?q=" + artistNameURL + "&per_page=1"
                + "&token=" + API_KEY)

        val artistItem = Artist(artistString, null)
        Artist.artistArrayList.add(artistItem)

        var currentSongIndex = 0
        if (Artist.artistArrayList.size > 1)
        {
            currentSongIndex = Artist.artistArrayList.size - 1
        }

        val client = AsyncHttpClient()
        val finalCurrentSongIndex = currentSongIndex
        client[requestURL, object : JsonHttpResponseHandler()
        {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON)
            {
                Log.d(TAG, "onSuccess")
                var artistPicture: String?
                val jsonObject = json.jsonObject
                try {
                    val results = jsonObject.getJSONArray("results")
                    val artistData = results.getJSONObject(0)
                    artistPicture = artistData.getString("cover_image")
                    if (artistPicture.contains(".gif"))
                    {
                        artistPicture = null
                    }
                }
                catch (e: JSONException)
                {
                    Log.e(TAG, "Hit json exception", e)
                    e.printStackTrace()
                    Artist.artistArrayList[finalCurrentSongIndex].profilePicture = null.toString()
                    return
                }
                if (artistPicture != null)
                {
                    Artist.artistArrayList[finalCurrentSongIndex].profilePicture = artistPicture
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers,
                response: String,
                throwable: Throwable
            )
            {
                Log.d(TAG, "Failed!")
                Log.d(TAG, throwable.message!!)
            }
        }]
    }

    fun areSongsReady(): Boolean
    {
        return songState.value == SongInitializationState.INITIALIZED
    }
}
