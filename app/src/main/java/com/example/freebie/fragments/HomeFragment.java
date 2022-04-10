package com.example.freebie.fragments;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.freebie.R;
import com.example.freebie.SongsAdapter;
import com.example.freebie.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private RecyclerView rvSongs;
    private ProgressBar progressBar;
    private List<Song> allSongs;
    private SongsAdapter adapter;

    private FragmentManager fragmentManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();

        rvSongs = view.findViewById(R.id.rvSongs);
        progressBar = view.findViewById(R.id.progressBar);

        allSongs = new ArrayList<>();
        adapter = new SongsAdapter(getContext(), allSongs);

        rvSongs.setAdapter(adapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        // Refresh song list
        updateSongsOnSeparateThread();
    }

    public void updateSongsOnSeparateThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Work to do
                Log.i(TAG, "Loading songs...");
                ArrayList<Song> songs = updateSongs();

                // Check if fragment is active
                Fragment fragment = fragmentManager.findFragmentByTag(TAG);
                if(fragment == null) {
                    Log.w(TAG, "Breaking out of thread, fragment switched during loading");
                    return;
                }

                // Post execution
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Remember to CLEAR OUT old items before appending in the new ones
                        adapter.clear();
                        allSongs.addAll(songs);

                        // Signal refresh has finished
                        adapter.notifyDataSetChanged();
                        // Disable loading bar when ready
                        progressBar.setVisibility(View.GONE);
                        Log.i(TAG, "Finished loading songs!");
                    }
                });
            }
        }).start();
    }

    public ArrayList<Song> updateSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri filePathUri;
        String filePath = "Unknown";
        Cursor songCursor = getContext().getContentResolver().query(songUri, null, null, null, null);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        if(songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                // Retrieve song path
                int column_index = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                filePathUri = Uri.parse(songCursor.getString(column_index));
                filePath = filePathUri.getPath();

                // Set the working file
                mediaMetadataRetriever.setDataSource(filePath);

                // Get album art and convert it to a bitmap
                Bitmap albumBitmap = null;
                byte[] albumArtData = mediaMetadataRetriever.getEmbeddedPicture();

                if (albumArtData != null) {
                    albumBitmap = BitmapFactory.decodeByteArray(albumArtData, 0, albumArtData.length);
                    albumBitmap = Bitmap.createScaledBitmap(albumBitmap, 128, 128, false);
                }

                // Retrieve song length
                String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long rawLength = Long.parseLong(duration);
                String seconds = String.valueOf((rawLength % 60000) / 1000);
                String minutes = String.valueOf(rawLength / 60000);
                String length;
                if(seconds.length() == 1)
                    length = minutes + ":" + "0" + seconds;
                else
                    length = minutes + ":" + seconds;

                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
                String album = songCursor.getString(songAlbum);
                songs.add(new Song(title, artist, album, length, filePath, albumBitmap));
            } while (songCursor.moveToNext());
        }
        songCursor.close();
        mediaMetadataRetriever.release();

        return songs;
    }
}