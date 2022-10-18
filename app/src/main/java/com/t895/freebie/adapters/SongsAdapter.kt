package com.t895.freebie.adapters

import android.content.Context
import android.util.Log
import com.t895.freebie.models.Song
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import android.view.ViewGroup
import android.view.LayoutInflater
import com.t895.freebie.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.t895.freebie.databinding.ItemSongBinding
import com.t895.freebie.utils.InsetsHelper

class SongsAdapter(private val context: Context, var songs: LinkedHashMap<Int, Song>) :
RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private val TAG = "SongsAdapter"

    private lateinit var keys: IntArray

    private val requestOptions: RequestOptions = RequestOptions().transform(
        RoundedCorners(InsetsHelper.dpToPx(context, InsetsHelper.EIGHT_DP))
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        keys = songs.keys.toIntArray()
        return ViewHolder(ItemSongBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[keys[position]]!!)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class ViewHolder(private val itemBinding: ItemSongBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(song: Song) {
            itemBinding.apply {
                tvTitle.text = song.title
                tvArtist.text = song.artist
                tvSongLength.text = song.length
            }

            itemBinding.btnSong.setOnClickListener {
                Log.d(TAG, song.title + " was clicked!")
            }

            Glide.with(context)
                .load(song.uri)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .into(itemBinding.ivSongCover)
        }
    }

    fun swapData(songs: LinkedHashMap<Int, Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }
}
