package com.example.freebie.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Song {

    public String title;
    public String length;
//    public long id; // MBID? if necessary
    public String artist;
    public String genre;

    public Song(){
        title = "example song title";
        length = "2" + ":" + "45";
//        id = 123456789;
        artist = "example artist name";
        genre = "example genre";
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

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
