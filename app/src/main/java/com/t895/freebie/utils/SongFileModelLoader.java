package com.t895.freebie.utils;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.nio.ByteBuffer;

public final class SongFileModelLoader implements ModelLoader<String, ByteBuffer>
{
  private static final String DATA_URI_PREFIX = "song:";

  @Nullable
  @Override
  public LoadData<ByteBuffer> buildLoadData(String model, int width, int height, Options options)
  {
    return new LoadData<>(new ObjectKey(model), new SongDataFetcher(model));
  }

  @Override
  public boolean handles(String model)
  {
    return model.startsWith(DATA_URI_PREFIX);
  }
}