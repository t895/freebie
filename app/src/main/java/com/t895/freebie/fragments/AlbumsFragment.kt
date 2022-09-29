package com.t895.freebie.fragments

import androidx.recyclerview.widget.RecyclerView
import com.t895.freebie.models.Album
import com.t895.freebie.adapters.AlbumsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.t895.freebie.AfterSongInitializationRunner
import com.t895.freebie.databinding.FragmentAlbumsBinding
import java.util.ArrayList

class AlbumsFragment : Fragment()
{
    private val TAG = "AlbumsFragment"

    private lateinit var allAlbums: ArrayList<Album>
    private lateinit var adapter: AlbumsAdapter

    private lateinit var mBinding: FragmentAlbumsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        mBinding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allAlbums = ArrayList()

        adapter = AlbumsAdapter(requireContext(), allAlbums)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        mBinding.rvAlbums.adapter = adapter
        mBinding.rvAlbums.layoutManager = GridLayoutManager(context, 2)
        refreshAlbums()
    }

    private fun refreshAlbums()
    {
        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear()
        AfterSongInitializationRunner().runWithLifecycle(activity) { adapter.addAll(Album.albumArrayList) }
    }
}
