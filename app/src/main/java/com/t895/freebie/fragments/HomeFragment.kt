package com.t895.freebie.fragments

import com.t895.freebie.adapters.SongsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.t895.freebie.models.Song
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.t895.freebie.AfterSongInitializationRunner
import com.t895.freebie.databinding.FragmentHomeBinding
import java.util.ArrayList

class HomeFragment : Fragment()
{
    private val TAG = "HomeFragment"

    private lateinit var adapter: SongsAdapter

    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allSongs = ArrayList<Song>()

        adapter = SongsAdapter(requireContext(), allSongs)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        mBinding.rvSongs.adapter = adapter
        mBinding.rvSongs.layoutManager = LinearLayoutManager(context)
        refreshSongs()
    }

    private fun refreshSongs()
    {
        adapter.clear()
        AfterSongInitializationRunner().runWithLifecycle(activity) { adapter.addAll(Song.songArrayList) }
    }
}
