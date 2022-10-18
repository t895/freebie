package com.t895.freebie.fragments

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
import com.t895.freebie.utils.InsetsHelper

class AlbumsFragment : Fragment() {
    private val TAG = "AlbumsFragment"
    private lateinit var mBinding: FragmentAlbumsBinding

    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InsetsHelper.setListInsets(mBinding.rvAlbums, requireContext())
        albumsAdapter = AlbumsAdapter(requireContext(), LinkedHashMap())
        AfterSongInitializationRunner().runWithLifecycle(activity) {
            albumsAdapter.swapData(Album.list)
            mBinding.rvAlbums.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = albumsAdapter
            }
        }
    }
}
