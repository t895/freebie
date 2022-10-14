package com.t895.freebie.models

data class Song(
    val title: String,
    val artist: String,
    val album: String,
    val length: String,
    val uri: String
) {
    companion object {
        var list = LinkedHashMap<Int, Song>()
    }
}
