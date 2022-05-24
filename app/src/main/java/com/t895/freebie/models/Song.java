package com.t895.freebie.models;

import java.util.ArrayList;

public class Song {

    public static ArrayList<Song> songArrayList = new ArrayList<>();

    private String title;
    private String artist;
    private String length;
    private String uri;

    public Song(String title, String artist, String length, String uri){
        this.title = title;
        this.artist = artist;
        this.length = length;
        this.uri = uri;
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getLength() { return length; }

    public String getUri() { return uri; }
}
