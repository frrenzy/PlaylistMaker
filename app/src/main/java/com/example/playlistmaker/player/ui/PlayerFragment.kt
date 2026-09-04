package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.data.Constants
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.presentation.AddTrackToPlaylistResult
import com.example.playlistmaker.player.presentation.PlayerState
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.dp
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private val playlistsAdapter = PlaylistsSmallAdapter {
        viewModel.onPlaylistClick(it)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPlayerBinding.inflate(inflater, container, false)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observeTrack().observe(viewLifecycleOwner) {
            drawTrack(it)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            renderPlayer(it)
        }

        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            playlistsAdapter.playlists = it
            playlistsAdapter.notifyDataSetChanged()
        }
        viewModel.loadPlaylists()

        viewModel.observeMessage().observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                if (it is AddTrackToPlaylistResult.Success) {
                    binding.overlay.isVisible = false
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.loadPlaylists()
                }

                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
            }
        }

        with(binding) {
            playButton.setOnClickListener {
                viewModel.onPlayButtonClick()
            }

            likeButton.setOnClickListener {
                viewModel.onLikeButtonClick()
            }

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            setFragmentResultListener(Constants.CREATION_RESULT) { _, _ ->
                bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_COLLAPSED
                overlay.alpha = slideToOverlayAlpha(0f)
                overlay.isVisible = true
            }

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(sheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> overlay.isVisible = false
                        else -> overlay.isVisible = true
                    }
                }

                override fun onSlide(sheet: View, slideOffset: Float) {
                    overlay.alpha = slideToOverlayAlpha(slideOffset)
                }
            })

            addButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            }

            playlistsList.adapter = playlistsAdapter
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

    private fun slideToOverlayAlpha(slide: Float) = (slide + 1f) / 2

    companion object {
        private const val TRACK_ART_CORNER_RADIUS = 8
        private const val PLAYER_TRACK_KEY = "track"

        fun createArgs(track: Track) = Bundle().apply {
            putParcelable(PLAYER_TRACK_KEY, track)
        }
    }
}
