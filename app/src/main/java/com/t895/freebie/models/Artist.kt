package com.t895.freebie.models

data class Artist(
    val name: String,
    var profilePicture: String?
) {
    companion object {
        var list = LinkedHashMap<Int, Artist>()
    }
}
