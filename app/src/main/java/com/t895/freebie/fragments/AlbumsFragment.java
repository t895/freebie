package com.t895.freebie.fragments;

import static com.t895.freebie.MainActivity.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t895.freebie.adapter.AlbumsAdapter;
import com.t895.freebie.R;
import com.t895.freebie.SongRetrievalService;
import com.t895.freebie.models.Album;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {

    private static final String TAG = "AlbumsFragment";

    private RecyclerView rvAlbums;
    private ArrayList<Album> allAlbums;
    private AlbumsAdapter adapter;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAlbums = view.findViewById(R.id.rvAlbums);

        allAlbums = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), allAlbums);
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        rvAlbums.setAdapter(adapter);
        rvAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2));

        refreshAlbums(savedInstanceState);
    }

    public void refreshAlbums(Bundle savedInstanceState) {
        Log.i(TAG, "Rebuilding list!");
        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();

        Thread RefreshingHomeFragment = new Thread(new Runnable() {
            @Override
            public void run() {
                // Just load the current values if nothing from disk is being loaded
                if(!SongRetrievalService.loadingSongs)
                    mainActivity.runOnUiThread(() -> adapter.addAll(Album.albumArrayList));

                // Check for edge case during configuration change happens during disk load
                if(savedInstanceState != null)
                    return;

                while(SongRetrievalService.loadingSongs) {
                    int startSize = adapter.albums.size();
                    int endSize = Album.albumArrayList.size();
                    if(startSize < endSize) {
                        mainActivity.runOnUiThread(() -> {
                            for (int i = adapter.albums.size(); i < Album.albumArrayList.size(); i++) {
                                adapter.add(Album.albumArrayList.get(i));
                                adapter.notifyItemInserted(i);
                            }
                        });
                    }
                }
                Log.i(TAG, "Finished loading list with " + adapter.albums.size() + " songs!");
            }
        });
        RefreshingHomeFragment.start();
    }
}