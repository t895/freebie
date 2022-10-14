package com.t895.freebie.models

data class Album(
    val title: String,
    val artist: String,
    val uri: String
) {

    companion object {
        var list = LinkedHashMap<Int, Album>()
    }
}
