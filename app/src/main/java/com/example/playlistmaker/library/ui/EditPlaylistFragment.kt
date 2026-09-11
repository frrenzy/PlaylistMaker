package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.presentation.EditPlaylistViewModel
import com.example.playlistmaker.utils.getCoverImageFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment() {
    // override required for pickMediaRequest closure
    override val viewModel: EditPlaylistViewModel by viewModel {
        val playlistId = requireArguments().getLong(PLAYLIST_KEY)
        parametersOf(playlistId)
    }

    override fun setupListeners() {
        with(viewModel) {
            observeValidity().observe(viewLifecycleOwner) {
                binding.createButton.isEnabled = it
            }

            observeMessage().observe(viewLifecycleOwner) { message ->
                message.getContentIfNotHandled()?.let {
                    findNavController().navigateUp()
                }
            }

            observePlaylist().observe(viewLifecycleOwner) { message ->
                message.getContentIfNotHandled()?.let { render(it) }
            }
            loadPlaylist()
        }

        with(binding) {
            playlistCover.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            backButton.setOnClickListener { findNavController().navigateUp() }

            createButton.setOnClickListener { viewModel.onCreateClick() }
            name.editText?.doOnTextChanged { s, _, _, _ -> viewModel.setName(s) }
            description.editText?.doOnTextChanged { s, _, _, _ -> viewModel.setDescription(s) }
        }
    }

    private fun render(playlist: Playlist) = with(binding) {
        createButton.text = getString(R.string.playlist_edit_button)
        title.text = getString(R.string.playlist_edit_title)

        val coverImage = getCoverImageFile(requireActivity(), playlist.coverPath)
        Glide.with(root)
            .load(coverImage)
            .signature(ObjectKey(coverImage?.lastModified() ?: 0))
            .placeholder(R.drawable.ic_placeholder_45)
            .transform(FitCenter())
            .into(image)

        name.editText?.setText(playlist.name)
        description.editText?.setText(playlist.description)
    }

    companion object {
        private const val PLAYLIST_KEY = "playlist"

        fun createArgs(playlistId: Long) = Bundle().apply {
            putLong(PLAYLIST_KEY, playlistId)
        }
    }
}
