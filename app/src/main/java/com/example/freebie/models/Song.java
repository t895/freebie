package com.example.freebie.models;

import android.graphics.Bitmap;

public class Song {

    public String title;
    public String artist;
    public String album;
    public String length;
    public String path;
    public Bitmap albumArt;

    public Song(String title, String artist, String album, String length, String path, Bitmap albumArt){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.path = path;
        this.albumArt = albumArt;
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getAlbum() { return album; }

    public String getLength() { return length; }

    public String getPath() { return path; }

    public Bitmap getAlbumArt() { return albumArt; }
}
