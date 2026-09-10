package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.TrackAdapter
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.presentation.PlaylistViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.utils.getCoverImageFile
import com.example.playlistmaker.utils.ui.BindingFragment
import com.example.playlistmaker.utils.ui.dp
import com.example.playlistmaker.utils.ui.slideToOverlayAlpha
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {
    private val viewModel: PlaylistViewModel by viewModel {
        val playlistId = requireArguments().getLong(PLAYLIST_KEY)
        parametersOf(playlistId)
    }

    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var confirmTrackDeletionDialog: MaterialAlertDialogBuilder
    private lateinit var confirmPlaylistDeletionDialog: MaterialAlertDialogBuilder

    private val trackAdapter = TrackAdapter(
        onLongClickListener = { removeTrack(it.trackId) },
        onClickListener = { openPlayer(it) },
    )
    private var trackIdToDelete: Long? = null
    private var playlistName: String? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            val (playlist, tracks) = it
            renderPlaylist(playlist, tracks)
            playlistName = playlist.name
        }
        viewModel.loadPlaylist()

        confirmTrackDeletionDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.playlist_delete_track_dialog_question))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.removeTrackFromPlaylist(trackIdToDelete)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }

        confirmPlaylistDeletionDialog = MaterialAlertDialogBuilder(requireActivity())
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist(requireActivity())
                findNavController().navigateUp()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }

        with(binding) {
            trackList.adapter = trackAdapter

            backButton.setOnClickListener { findNavController().navigateUp() }

            shareButton.setOnClickListener { viewModel.sharePlaylist() }

            menuButton.setOnClickListener {
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            menuBottomSheetBehavior.addBottomSheetCallback(object :
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
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                overlay.isVisible = false
            }

            menuShare.setOnClickListener { viewModel.shareApp() }
            menuDelete.setOnClickListener {
                confirmPlaylistDeletionDialog
                    .setTitle(
                        getString(
                            R.string.playlist_delete_playlist_dialog_question,
                            playlistName
                        )
                    ).show()
            }
            menuEdit.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playlistFragment_to_editPlaylistFragment,
                    EditPlaylistFragment.createArgs(
                        requireArguments().getLong(PLAYLIST_KEY)
                    )
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderPlaylist(playlist: Playlist, tracks: List<Track>) = with(binding) {
        val coverImage = getCoverImageFile(requireActivity(), playlist.coverPath)
        val preparedImage = Glide.with(root)
            .load(coverImage)
            .signature(ObjectKey(coverImage?.lastModified() ?: 0))
            .placeholder(R.drawable.ic_placeholder_45)

        /* Main area */
        preparedImage
            .transform(CenterCrop())
            .into(image)

        name.text = playlist.name
        playlist.description?.let {
            description.text = it
        } ?: run {
            description.isVisible = false
        }

        val playTime = tracks.fold(0) { acc, track -> acc + track.trackTimeMillis }
        val playTimeInMinutes =
            Track.trackTimeFormat
                .format(playTime)
                .substringBefore(':')
                .toInt()
        length.text = resources.getQuantityString(
            R.plurals.playlist_length,
            playTimeInMinutes, playTimeInMinutes,
        )
        amount.text =
            resources.getQuantityString(
                R.plurals.playlist_track_amount,
                tracks.size, tracks.size,
            )

        /* Menu card */
        preparedImage
            .transform(
                CenterCrop(),
                RoundedCorners(MENU_CARD_IMAGE_CORNER_RADIUS.dp),
            )
            .into(cardImage)

        cardName.text = playlist.name
        cardAmount.text = resources.getQuantityString(
            R.plurals.playlist_track_amount,
            tracks.size, tracks.size,
        )

        /* Track list */
        if (tracks.isEmpty()) {
            tracksBottomSheet.isVisible = false
            mainContent.setPadding(0, 0, 0, 0)
        }
        trackAdapter.tracks = tracks
        trackAdapter.notifyDataSetChanged()
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun removeTrack(trackId: Long) {
        trackIdToDelete = trackId
        confirmTrackDeletionDialog.show()
    }

    companion object {
        private const val PLAYLIST_KEY = "playlist"
        private const val MENU_CARD_IMAGE_CORNER_RADIUS = 2

        fun createArgs(playlistId: Long) = Bundle().apply {
            putLong(PLAYLIST_KEY, playlistId)
        }
    }
}
