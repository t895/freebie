package com.t895.freebie.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Album {

    public static ArrayList<Album> albumArrayList = new ArrayList<>();

    private String title;
    private String artist;
    private String uri;

    public Album(String title, String artist, String uri){
        this.title = title;
        this.artist = artist;
        this.uri = uri;
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getUri() { return uri; }
}
