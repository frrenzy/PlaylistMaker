package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : BindingFragment<FragmentLibraryBinding>() {
    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLibraryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.library_tab_favourite)
                1 -> getString(R.string.library_tab_playlists)
                else -> ""
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}
