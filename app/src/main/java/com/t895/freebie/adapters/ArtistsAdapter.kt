package com.t895.freebie.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import android.view.ViewGroup
import android.view.LayoutInflater
import com.t895.freebie.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.t895.freebie.databinding.ItemArtistBinding
import com.t895.freebie.models.Artist
import java.lang.Exception

class ArtistsAdapter(private val context: Context, var artists: LinkedHashMap<Int, Artist>) :
    RecyclerView.Adapter<ArtistsAdapter.ViewHolder>() {
    private val TAG = "ArtistsAdapter"

    private lateinit var keys: IntArray

    private val requestOptions: RequestOptions =
        RequestOptions().transform(CenterCrop(), CircleCrop())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        keys = artists.keys.toIntArray()
        return ViewHolder(ItemArtistBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artists[keys[position]]!!)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    inner class ViewHolder(private val itemBinding: ItemArtistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(artist: Artist) {
            itemBinding.tvArtistName.text = artist.name

            itemBinding.btnArtist.setOnClickListener {
                try {
                    Log.i(TAG, "Artist " + artist.name + " was pressed!")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            Glide.with(context)
                .load(artist.profilePicture)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .transition(DrawableTransitionOptions.withCrossFade(50))
                .into(itemBinding.ivArtist)
        }
    }

    // Add a list of items -- change to type used
    fun swapData(artists: LinkedHashMap<Int, Artist>) {
        this.artists = artists
        notifyDataSetChanged()
    }
}
