package com.t895.freebie.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.t895.freebie.R
import com.t895.freebie.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment()
{
    private val TAG = "SettingsFragment"

    private lateinit var mBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }
}
