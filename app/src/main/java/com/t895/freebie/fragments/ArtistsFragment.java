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

import com.t895.freebie.R;
import com.t895.freebie.SongRetrievalService;
import com.t895.freebie.adapters.ArtistsAdapter;
import com.t895.freebie.models.Artist;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment
{

  private static final String TAG = "ArtistsFragment";

  private RecyclerView rvArtists;
  private ArrayList<Artist> allArtists;
  private ArtistsAdapter adapter;

  public ArtistsFragment()
  {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_artists, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);

    rvArtists = view.findViewById(R.id.rvArtists);

    allArtists = new ArrayList<>();
    adapter = new ArtistsAdapter(getContext(), allArtists);
    adapter.setStateRestorationPolicy(
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

    rvArtists.setAdapter(adapter);
    rvArtists.setLayoutManager(new GridLayoutManager(getContext(), 2));

    refreshArtists(savedInstanceState);
  }

  public void refreshArtists(Bundle savedInstanceState)
  {
    // Remember to CLEAR OUT old items before appending in the new ones
    adapter.clear();

    new Thread(() ->
    {
      // Just load the current values if nothing from disk is being loaded
      if (!SongRetrievalService.loadingSongs)
        mainActivity.runOnUiThread(() -> adapter.addAll(Artist.artistArrayList));

      // Check for edge case during configuration change happens during disk load
      if (savedInstanceState != null)
        return;

      while (SongRetrievalService.loadingSongs)
      {
        int startSize = adapter.artists.size();
        int endSize = Artist.artistArrayList.size();
        if (startSize < endSize)
        {
          mainActivity.runOnUiThread(() ->
          {
            for (int i = adapter.artists.size(); i < Artist.artistArrayList.size(); i++)
            {
              adapter.add(Artist.artistArrayList.get(i));
              adapter.notifyItemInserted(i);
            }
          });
        }
      }
    }).start();
  }
}