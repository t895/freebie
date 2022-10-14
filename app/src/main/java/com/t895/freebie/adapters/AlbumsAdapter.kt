package com.t895.freebie.adapters

import android.content.Context
import android.util.Log
import com.t895.freebie.utils.RoundedCornerHelper.dpToPx
import com.t895.freebie.models.Album
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import android.view.ViewGroup
import android.view.LayoutInflater
import com.t895.freebie.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.t895.freebie.databinding.ItemAlbumBinding
import com.t895.freebie.utils.RoundedCornerHelper
import java.util.ArrayList

class AlbumsAdapter(private val context: Context, var albums: LinkedHashMap<Int, Album>) :
    RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {
    private val TAG = "AlbumsAdapter"

    private lateinit var keys: IntArray

    private val requestOptions: RequestOptions = RequestOptions().transform(
        CenterCrop(),
        RoundedCorners(dpToPx(context, RoundedCornerHelper.EIGHT_DP))
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        keys = albums.keys.toIntArray()
        return ViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[keys[position]]!!)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class ViewHolder(private val itemBinding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(album: Album) {
            itemBinding.tvAlbumTitle.text = album.title
            itemBinding.tvAlbumArtist.text = album.artist

            itemBinding.btnAlbum.setOnClickListener {
                Log.i(TAG, "Album " + album.title + " was clicked!")
            }

            Glide.with(context)
                .load(album.uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loading)
                .transition(DrawableTransitionOptions.withCrossFade(50))
                .into(itemBinding.ivAlbum)
        }
    }

    // Clean all elements of the recycler
    fun clear() {
        albums.clear()
        notifyDataSetChanged()
    }

    // Add a list of items
    fun addAll(albums: LinkedHashMap<Int, Album>) {
        this.albums.putAll(albums)
        notifyDataSetChanged()
    }
}
