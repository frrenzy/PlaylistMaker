package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.presentation.PlaylistsState
import com.example.playlistmaker.library.presentation.PlaylistsViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val viewModel: PlaylistsViewModel by activityViewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding = FragmentPlaylistsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Playlists -> showPlaylists(state.playlists)
            is PlaylistsState.Empty -> showEmptyMessage()
        }
    }

    private fun showEmptyMessage() {
        binding.emptyBlock.isVisible = true
        binding.playlistList.isVisible = false
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.emptyBlock.isVisible = false
        binding.playlistList.isVisible = true
    }
}
