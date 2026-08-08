package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.TrackAdapter
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.library.presentation.FavouritesState
import com.example.playlistmaker.library.presentation.FavouritesViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavouritesBinding.inflate(inflater, container, false)

    private val viewModel: FavouritesViewModel by activityViewModel()

    private val favouritesAdapter = TrackAdapter {
        openPlayer(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavouritesState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.loadTracks()

        binding.trackList.adapter = favouritesAdapter
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

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: List<Track>) {
        binding.emptyBlock.isVisible = false
        binding.trackList.isVisible = true
        favouritesAdapter.tracks = tracks
        favouritesAdapter.notifyDataSetChanged()
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }
}
