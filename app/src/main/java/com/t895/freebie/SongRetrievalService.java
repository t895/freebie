package com.t895.freebie;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.t895.freebie.models.Album;
import com.t895.freebie.models.Artist;
import com.t895.freebie.models.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;

public class SongRetrievalService
{

  public static final String TAG = "SongRetrievalService";

  public static final String BASE_URL = "https://api.discogs.com/database/";
  public static final String API_KEY = "DHqvPIxOJwmIMQHtHkkoewRydAnSCRwFXnNAOUCI";

  public static final String SONGS_LOADED = "songs_loaded";
  public static final String SIZE_KEY = "size";

  public static boolean loadingSongs = false;

  public static void getAllSongs(Context context)
  {
    // Check if previously loaded songs are still in memory
    SharedPreferences sharedPreferences =
            context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE);
    int previousListSize = sharedPreferences.getInt(SIZE_KEY, 0);
    if (previousListSize == Song.songArrayList.size() && Song.songArrayList.size() != 0)
      return;

    loadingSongs = true;

    // Remove stale data
    Song.songArrayList.clear();
    Album.albumArrayList.clear();
    Artist.artistArrayList.clear();

    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    String[] projection = new String[]
            {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION
            };
    String selection = MediaStore.Video.Media.DURATION + " >= ?";
    String[] selectionArgs = new String[]
            {

            };

    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    try (Cursor songCursor = context.getContentResolver().query(
            uri,
            projection,
            null,
            null,
            null))
    {
      if (songCursor.moveToFirst())
      {
        int songTitleColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int songArtistColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int songAlbumColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int songPathColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int songDurationColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

        Uri filePathUri;
        do
        {
          // Retrieve song path
          filePathUri = Uri.parse(songCursor.getString(songPathColumn));

          // Retrieve title, artist, and album
          String titleString = songCursor.getString(songTitleColumn);
          String artistString = songCursor.getString(songArtistColumn);
          String albumString = songCursor.getString(songAlbumColumn);
          String durationString = songCursor.getString(songDurationColumn);

          // Retrieve song length
          long rawLength = Long.parseLong(durationString);
          String seconds = String.valueOf((rawLength % 60000) / 1000);
          String minutes = String.valueOf(rawLength / 60000);
          String length;
          if (seconds.length() == 1)
            length = minutes + ":0" + seconds;
          else
            length = minutes + ":" + seconds;

          // Find unique artists to add into artist collection
          if (Artist.artistArrayList.size() == 0)
          {
            getArtistDetails(artistString);
          }
          else
          {
            for (int i = 0; i < Artist.artistArrayList.size(); i++)
            {
              if (Artist.artistArrayList.get(i).getName().equals(artistString))
                break;
              if (i == Artist.artistArrayList.size() - 1)
              {
                getArtistDetails(artistString);
              }
            }
          }

          // Find unique albums to add into album collection and decode relevant album art
          if (Album.albumArrayList.size() == 0)
          {
            Album albumItem = new Album(albumString, artistString, "song:" + filePathUri);
            Album.albumArrayList.add(albumItem);
          }
          else
          {
            for (int i = 0; i < Album.albumArrayList.size(); i++)
            {
              if (Album.albumArrayList.get(i).getTitle().equals(albumString))
                break;
              if (i == Album.albumArrayList.size() - 1)
              {
                Album albumItem = new Album(albumString, artistString, "song:" + filePathUri);
                Album.albumArrayList.add(albumItem);
              }
            }
          }
          Song song = new Song(titleString, artistString, albumString, length, "song:" + filePathUri);
          Song.songArrayList.add(song);
        }
        while (songCursor.moveToNext());
      }
    }
    try
    {
      mediaMetadataRetriever.release();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    loadingSongs = false;

    sharedPreferences = context.getSharedPreferences(SONGS_LOADED, Context.MODE_PRIVATE);
    SharedPreferences.Editor myEdit = sharedPreferences.edit();
    myEdit.putInt(SIZE_KEY, Song.songArrayList.size());
    myEdit.apply();
  }

  private static void getArtistDetails(String artistString)
  {
    // Put together elements of request URL
    String artistNameURL = artistString.replaceAll(" ", "%20");
    String requestURL = BASE_URL + "search?q=" + artistNameURL + "&per_page=1"
            + "&token=" + API_KEY;

    Artist artistItem = new Artist(artistString, null);
    Artist.artistArrayList.add(artistItem);

    int currentSongIndex = 0;
    if (Artist.artistArrayList.size() > 1)
      currentSongIndex = Artist.artistArrayList.size() - 1;

    AsyncHttpClient client = new AsyncHttpClient();
    int finalCurrentSongIndex = currentSongIndex;
    client.get(requestURL, new JsonHttpResponseHandler()
    {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json)
      {
        Log.d(TAG, "onSuccess");
        String artistPicture = null;
        JSONObject jsonObject = json.jsonObject;
        try
        {
          JSONArray results = jsonObject.getJSONArray("results");
          JSONObject artistData = results.getJSONObject(0);
          artistPicture = artistData.getString("cover_image");
          if (artistPicture.contains(".gif"))
            artistPicture = null;
        }
        catch (JSONException e)
        {
          Log.e(TAG, "Hit json exception", e);
          e.printStackTrace();
          Artist.artistArrayList.get(finalCurrentSongIndex).setProfilePicture(null);
          return;
        }
        Artist.artistArrayList.get(finalCurrentSongIndex).setProfilePicture(artistPicture);
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response,
              Throwable throwable)
      {
        Log.d(TAG, "Failed!");
        Log.d(TAG, throwable.getMessage());
      }
    });
  }
}
