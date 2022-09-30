package com.t895.freebie.models

import java.util.ArrayList

data class Artist(val name: String, var profilePicture: String?)
{
    companion object
    {
        var artistArrayList = ArrayList<Artist>()
    }
}
