package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.presentation.PlayerState
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.dp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    private val viewModel: PlayerViewModel by viewModel {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(PLAYER_TRACK_KEY, Track::class.java)
        } else {
            requireArguments().getParcelable(PLAYER_TRACK_KEY)
        }
        parametersOf(track)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPlayerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTrack().observe(viewLifecycleOwner) {
            drawTrack(it)
        }
        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            renderPlayer(it)
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClick()
        }
        binding.likeButton.setOnClickListener {
            viewModel.onLikeButtonClick()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPaused()
    }

    private fun drawTrack(track: Track) {
        binding.apply {
            trackName.text = track.trackName
            trackAuthor.text = track.artistName
            trackTime.text = track.trackTime
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

            track.year?.let {
                trackYear.text = it
            } ?: run {
                trackYearGroup.isVisible = false
            }

            track.collectionName?.let {
                trackAlbum.text = it
            } ?: run {
                trackAlbumGroup.isVisible = false
            }

            if (track.isFavourite) likeButton.setImageResource(R.drawable.ic_like_active_25)
            else likeButton.setImageResource(
                R.drawable.ic_like_inactive_25
            )

            Glide.with(this@PlayerFragment)
                .load(track.coverArtworkUrl)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(RoundedCorners(TRACK_ART_CORNER_RADIUS.dp))
                .into(trackCover)
        }
    }

    private fun renderPlayer(state: PlayerState) {
        binding.apply {
            playButton.isEnabled = state.isPlayButtonEnabled
            playTime.text = state.progressTime
            playButton.background = when (state) {
                is PlayerState.Playing -> getDrawable(
                    requireActivity(),
                    R.drawable.pause_button
                )

                else -> getDrawable(requireActivity(), R.drawable.play_button)
            }
        }
    }

    companion object {
        private const val TRACK_ART_CORNER_RADIUS = 8
        private const val PLAYER_TRACK_KEY = "track"

        fun createArgs(track: Track) = Bundle().apply {
            putParcelable(PLAYER_TRACK_KEY, track)
        }
    }
}
