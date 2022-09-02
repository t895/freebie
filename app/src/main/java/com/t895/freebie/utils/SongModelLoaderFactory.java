package com.t895.freebie.utils;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.nio.ByteBuffer;

public class SongModelLoaderFactory implements ModelLoaderFactory<String, ByteBuffer>
{
  @NonNull
  @Override
  public ModelLoader<String, ByteBuffer> build(@NonNull MultiModelLoaderFactory multiFactory)
  {
    return new SongFileModelLoader();
  }

  @Override
  public void teardown()
  { /* No-op */ }
}