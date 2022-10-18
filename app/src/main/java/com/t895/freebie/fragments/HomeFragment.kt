package com.t895.freebie.fragments

import com.t895.freebie.adapters.SongsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.t895.freebie.models.Song
import androidx.recyclerview.widget.LinearLayoutManager
import com.t895.freebie.AfterSongInitializationRunner
import com.t895.freebie.databinding.FragmentHomeBinding
import com.t895.freebie.utils.InsetsHelper

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var mBinding: FragmentHomeBinding

    private lateinit var songsAdapter: SongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InsetsHelper.setListInsets(mBinding.rvSongs, requireContext())
        songsAdapter = SongsAdapter(requireContext(), LinkedHashMap())
        AfterSongInitializationRunner().runWithLifecycle(activity) {
            songsAdapter.swapData(Song.list)
            mBinding.rvSongs.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = songsAdapter
            }
        }
    }
}
