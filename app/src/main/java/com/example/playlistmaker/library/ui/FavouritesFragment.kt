package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.library.presentation.FavouritesState
import com.example.playlistmaker.library.presentation.FavouritesViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>() {
    private val viewModel: FavouritesViewModel by activityViewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavouritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavouritesState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: FavouritesState) {
        when (state) {
            is FavouritesState.Tracks -> showTracks(state.tracks)
            is FavouritesState.Empty -> showEmptyMessage()
        }
    }

    private fun showEmptyMessage() {
        binding.emptyBlock.isVisible = true
        binding.trackList.isVisible = false
    }

    private fun showTracks(tracks: List<Track>) {
        binding.emptyBlock.isVisible = false
        binding.trackList.isVisible = true
    }
}
