package com.t895.freebie.fragments;

import static com.t895.freebie.MainActivity.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t895.freebie.R;
import com.t895.freebie.SongRetrievalService;
import com.t895.freebie.adapter.SongsAdapter;
import com.t895.freebie.models.Song;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    public static final String LIST_STATE_KEY = "home_recycler_list_state";
    Parcelable listState;

    private RecyclerView rvSongs;
    private ArrayList<Song> allSongs;
    private SongsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        rvSongs.setAdapter(adapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshSongs(savedInstanceState);
    }

    public void refreshSongs(Bundle savedInstanceState) {
        Log.i(TAG, "Rebuilding list!");
        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();

        Thread RefreshingHomeFragment = new Thread(new Runnable() {
            @Override
            public void run() {
                // Just load the current values if nothing from disk is being loaded
                if(!SongRetrievalService.loadingSongs) {
                    mainActivity.runOnUiThread(() -> adapter.addAll(Song.songArrayList));
                }

                // Check for edge case during configuration change happens during disk load
                if(savedInstanceState != null)
                    return;

                while(SongRetrievalService.loadingSongs) {
                    int startSize = adapter.songs.size();
                    int endSize = Song.songArrayList.size();
                    if(startSize < endSize) {
                        mainActivity.runOnUiThread(() -> {
                            for (int i = adapter.songs.size(); i < Song.songArrayList.size(); i++) {
                                adapter.add(Song.songArrayList.get(i));
                                adapter.notifyItemInserted(i);
                            }
                        });
                    }
                }
                Log.i(TAG, "Finished loading list with " + adapter.songs.size() + " songs!");
            }
        });
        RefreshingHomeFragment.start();
    }
}