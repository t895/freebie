package com.t895.freebie.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.nio.ByteBuffer;

@GlideModule
public class GlideSongModule extends AppGlideModule
{
  @Override
  public void registerComponents(Context context, Glide glide, Registry registry)
  {
    registry.replace(String.class, ByteBuffer.class, new SongModelLoaderFactory());
  }
}
