package com.t895.freebie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.t895.freebie.R;

public class SettingsFragment extends Fragment
{
  private static final String TAG = "SettingsFragment";

  public SettingsFragment()
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
    return inflater.inflate(R.layout.fragment_settings, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
  }
}