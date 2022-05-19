package com.t895.freebie.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Album {

    public static ArrayList<Album> albumArrayList = new ArrayList<>();

    private String title;
    private String artist;
    private Bitmap highResAlbumArt;
    private Bitmap lowResAlbumArt;

    public Album(String title, String artist, Bitmap highResAlbumArt, Bitmap lowResAlbumArt){
        this.title = title;
        this.artist = artist;
        this.highResAlbumArt = highResAlbumArt;
        this.lowResAlbumArt = lowResAlbumArt;
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public Bitmap getHighResAlbumArt() { return highResAlbumArt; }

    public Bitmap getLowResAlbumArt() { return lowResAlbumArt; }
}
