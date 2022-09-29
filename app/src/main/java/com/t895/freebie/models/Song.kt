package com.t895.freebie.models

import java.util.ArrayList

class Song(
    val title: String,
    val artist: String,
    val album: String,
    val length: String,
    val uri: String
)
{
    companion object
    {
        var songArrayList = ArrayList<Song>()
    }
}
