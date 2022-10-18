package com.t895.freebie.fragments

import com.t895.freebie.adapters.ArtistsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.t895.freebie.AfterSongInitializationRunner
import com.t895.freebie.databinding.FragmentArtistsBinding
import com.t895.freebie.models.Artist
import com.t895.freebie.utils.InsetsHelper

class ArtistsFragment : Fragment() {
    private val TAG = "ArtistsFragment"
    private lateinit var mBinding: FragmentArtistsBinding

    private lateinit var artistAdapter: ArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentArtistsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InsetsHelper.setListInsets(mBinding.rvArtists, requireContext())
        artistAdapter = ArtistsAdapter(requireContext(), LinkedHashMap())
        AfterSongInitializationRunner().runWithLifecycle(activity) {
            artistAdapter.swapData(Artist.list)
            mBinding.rvArtists.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = artistAdapter
            }
        }
    }
}
