package com.t895.freebie.models;

import java.util.ArrayList;

public class Song
{
  public static ArrayList<Song> songArrayList = new ArrayList<>();

  private String title;
  private String artist;
  private String album;
  private String length;
  private String uri;

  public Song(String title, String artist, String album, String length, String uri)
  {
    this.title = title;
    this.artist = artist;
    this.album = album;
    this.length = length;
    this.uri = uri;
  }

  public String getTitle()
  {
    return title;
  }

  public String getArtist()
  {
    return artist;
  }

  public String getAlbum()
  {
    return album;
  }

  public String getLength()
  {
    return length;
  }

  public String getUri()
  {
    return uri;
  }
}
