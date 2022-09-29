package com.t895.freebie.models

import java.util.ArrayList

class Album(val title: String, val artist: String, val uri: String)
{
    companion object
    {
        var albumArrayList = ArrayList<Album>()
    }
}
