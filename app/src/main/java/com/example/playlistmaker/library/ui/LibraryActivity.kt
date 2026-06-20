package com.example.playlistmaker.library.ui

import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.utils.BindingActivity
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : BindingActivity<ActivityLibraryBinding>() {
    override fun createBinding() = ActivityLibraryBinding.inflate(layoutInflater)
    override fun getBackButton() = binding.backButton
    
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.pager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.library_tab_favourite)
                1 -> getString(R.string.library_tab_playlists)
                else -> ""
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
