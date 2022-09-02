package com.t895.freebie.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Album implements Parcelable
{
  public static ArrayList<Album> albumArrayList = new ArrayList<>();

  private String title;
  private String artist;
  private String uri;

  public Album(String title, String artist, String uri)
  {
    this.title = title;
    this.artist = artist;
    this.uri = uri;
  }

  protected Album(Parcel in)
  {
    title = in.readString();
    artist = in.readString();
    uri = in.readString();
  }

  public static final Creator<Album> CREATOR = new Creator<Album>()
  {
    @Override
    public Album createFromParcel(Parcel in)
    {
      return new Album(in);
    }

    @Override
    public Album[] newArray(int size)
    {
      return new Album[size];
    }
  };

  public String getTitle()
  {
    return title;
  }

  public String getArtist()
  {
    return artist;
  }

  public String getUri()
  {
    return uri;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(title);
    dest.writeString(artist);
    dest.writeString(uri);
  }
}
