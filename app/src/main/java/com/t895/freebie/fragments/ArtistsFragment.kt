package com.t895.freebie.fragments

import androidx.recyclerview.widget.RecyclerView
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
import java.util.ArrayList

class ArtistsFragment : Fragment() {
    private val TAG = "ArtistsFragment"

    private var allArtists: LinkedHashMap<Int, Artist> = LinkedHashMap()
    private lateinit var adapter: ArtistsAdapter

    private lateinit var mBinding: FragmentArtistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentArtistsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArtistsAdapter(requireContext(), allArtists)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        mBinding.rvArtists.adapter = adapter
        mBinding.rvArtists.layoutManager = GridLayoutManager(context, 2)
        refreshArtists()
    }

    private fun refreshArtists() {
        adapter.clear()
        AfterSongInitializationRunner().runWithLifecycle(activity) { adapter.addAll(Artist.list) }
    }
}
