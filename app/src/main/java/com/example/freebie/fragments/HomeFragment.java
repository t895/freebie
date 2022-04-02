package com.example.freebie.fragments;

import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freebie.R;
import com.example.freebie.SongsAdapter;
import com.example.freebie.models.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView rvSongs;
    private List<Song> allSongs;
    private SwipeRefreshLayout swipeContainer;
    private SongsAdapter adapter;


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

        rvSongs = view.findViewById(R.id.rvSongs);

        allSongs = new ArrayList<>();
        adapter = new SongsAdapter(getContext(), allSongs);

        rvSongs.setAdapter(adapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.freebie_light_primary);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateSongs();
            }
        });
        // refresh song list (add a method in this class)
        updateSongs();
    }

    public void updateSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri filePathUri;
        String fileName = "Unknown";
        Cursor songCursor = getContext().getContentResolver().query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                int column_index = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(songCursor.getString(column_index));
                fileName = filePathUri.getPath().toString();

                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
                Log.i(TAG, "Song path: " + fileName);
                songs.add(new Song(title, artist, fileName));
            } while (songCursor.moveToNext());
        }

        songCursor.close();

        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();
        allSongs.addAll(songs);

        // Signal refresh has finished
        swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }
}