package com.t895.freebie.utils;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SongDataFetcher implements DataFetcher<ByteBuffer>
{
  private static final String TAG = "SongDataFetcher";

  private final String model;
  MediaMetadataRetriever mediaMetadataRetriever;

  public SongDataFetcher(String model)
  {
    this.model = model;
  }

  @Override
  public void loadData(@NonNull Priority priority,
          @NonNull DataCallback<? super ByteBuffer> callback)
  {
    mediaMetadataRetriever = new MediaMetadataRetriever();
    // Remove 'song:' at the beginning of the URI
    mediaMetadataRetriever.setDataSource(model.substring(5));
    // Get the picture ready for Glide rendering
    byte[] data = mediaMetadataRetriever.getEmbeddedPicture();

    ByteBuffer byteBuffer = null;
    try {
      byteBuffer = ByteBuffer.wrap(data);
    } catch (NullPointerException e) { Log.e(TAG, "Tried to load null image"); }

    callback.onDataReady(byteBuffer);
  }

  @Override
  public void cleanup()
  {
    try
    {
      mediaMetadataRetriever.release();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void cancel()
  { /* No-op */ }

  @NonNull
  @Override
  public Class<ByteBuffer> getDataClass()
  {
    return ByteBuffer.class;
  }

  @NonNull
  @Override
  public DataSource getDataSource()
  {
    return DataSource.LOCAL;
  }
}
