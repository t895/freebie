package com.example.freebie.models;

import android.graphics.Bitmap;

public class Song {

    public String title;
    public String artist;
    public String path;
    public Bitmap albumArt;
    public String length;
    public String genre;

    public Song(String title, String artist, String path, Bitmap albumArt){
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.albumArt = albumArt;
        length = "2:45";
        genre = "example genre";
    }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getPath() { return path; }

    public Bitmap getAlbumArt() { return albumArt; }

    public String getLength() { return length; }

    public String getGenre() { return genre; }

    //    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
//        Tweet tweet = new Tweet();
//        tweet.body = jsonObject.getString("text");
//        tweet.createdAt = jsonObject.getString("created_at");
//        tweet.id = jsonObject.getLong("id");
//        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
//        tweet.time = RelativeTimeHelper.getRelativeTimeAgo(jsonObject.getString("created_at"));
//        return tweet;
//    }
//
//    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
//        List<Tweet> tweets = new ArrayList<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            tweets.add(fromJson(jsonArray.getJSONObject(i)));
//        }
//        return tweets;
//    }
}
