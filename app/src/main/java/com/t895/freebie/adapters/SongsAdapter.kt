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
import com.t895.freebie.utils.RoundedCornerHelper
import java.util.ArrayList

class SongsAdapter(private val context: Context, var songs: ArrayList<Song>) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>()
{
    private val TAG = "SongsAdapter"

    private val requestOptions: RequestOptions = RequestOptions().transform(
        RoundedCorners(RoundedCornerHelper.dpToPx(context, RoundedCornerHelper.EIGHT_DP)))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(ItemSongBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int
    {
        return songs.size
    }

    inner class ViewHolder(private val itemBinding: ItemSongBinding) : RecyclerView.ViewHolder(itemBinding.root)
    {
        fun bind(song: Song) {
            itemBinding.tvTitle.text = song.title
            itemBinding.tvArtist.text = song.artist
            itemBinding.tvSongLength.text = song.length

            itemBinding.btnSong.setOnClickListener {
                Log.d(TAG, song.title + " was clicked!")
            }

            Glide.with(context)
                .load(song.uri)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .into(itemBinding.ivAlbum)
        }
    }

    // Clean all elements of the recycler
    fun clear()
    {
        songs.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(songs: List<Song>?)
    {
        this.songs.addAll(songs!!)
        notifyDataSetChanged()
    }
}
