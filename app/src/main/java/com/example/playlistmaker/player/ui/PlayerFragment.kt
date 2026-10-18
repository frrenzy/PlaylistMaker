package com.example.playlistmaker.player.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.playlistmaker.player.services.MusicService
import com.example.playlistmaker.utils.ui.BindingFragment
import com.example.playlistmaker.utils.ui.dp
import com.example.playlistmaker.utils.ui.slideToOverlayAlpha
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    private val viewModel: PlayerViewModel by viewModel {
        val track = getTrackFromArgs()
        parametersOf(track)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder

            viewModel.setMusicPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.can_not_start_foreground_service), Toast.LENGTH_LONG
            )
                .show()
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        with(viewModel) {
            observeTrack().observe(viewLifecycleOwner) {
                drawTrack(it)
            }
            loadTrack()

            observePlayerState().observe(viewLifecycleOwner) {
                renderPlayer(it)
            }

            observePlaylists().observe(viewLifecycleOwner) {
                playlistsAdapter.playlists = it
                playlistsAdapter.notifyDataSetChanged()
            }
            loadPlaylists()

            observeMessage().observe(viewLifecycleOwner) { message ->
                message.getContentIfNotHandled()?.let {
                    if (it is AddTrackToPlaylistResult.Success) {
                        binding.overlay.isVisible = false
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }

                    Toast.makeText(
                        requireActivity(),
                        requireActivity().getString(it.stringId, it.arg),
                        Toast.LENGTH_LONG
                    ).show()
                }
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
            overlay.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                overlay.isVisible = false
            }

            addButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.loadPlaylists()
            }

            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            }

            overlay.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            playlistsList.adapter = playlistsAdapter
        }
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

    override fun onDestroyView() {
        super.onDestroyView()

        unbindMusicService()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onFragmentPaused()
    }


    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResumed()
    }

    private fun getTrackFromArgs() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requireArguments().getParcelable(PLAYER_TRACK_KEY, Track::class.java)
    } else {
        requireArguments().getParcelable(PLAYER_TRACK_KEY)
    }

    private fun renderPlayer(state: PlayerState) {
        binding.apply {
            playButton.isEnabled = state.isPlayButtonEnabled
            playTime.text = state.progressTime
            if (state is PlayerState.Prepared) {
                playButton.prepare()
            }
        }
    }

    private fun bindMusicService() {
        with(requireContext()) {
            val intent = MusicService.createIntent(this, getTrackFromArgs())
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    companion object {
        private const val TRACK_ART_CORNER_RADIUS = 8
        private const val PLAYER_TRACK_KEY = "track"

        fun createArgs(track: Track) = Bundle().apply {
            putParcelable(PLAYER_TRACK_KEY, track)
        }
    }
}
