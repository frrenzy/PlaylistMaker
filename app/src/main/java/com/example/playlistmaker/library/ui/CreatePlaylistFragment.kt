package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.data.Constants
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.library.presentation.CreatePlaylistViewModel
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {
    private val viewModel: CreatePlaylistViewModel by activityViewModel()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setCoverPath(uri)
                binding.image.setImageURI(uri)
                binding.image.tag = true
            }
        }

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCreatePlaylistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeValidity().observe(viewLifecycleOwner) {
            binding.createButton.isEnabled = it
        }

        viewModel.observeMessage().observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                exit()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.playlist_creation_confirm_dialog_title))
            .setMessage(getString(R.string.playlist_creation_confirm_dialog_message))
            .setNeutralButton(getString(R.string.playlist_creation_confirm_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.playlist_creation_confirm_dialog_confirm)) { _, _ ->
                exit()
            }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.createPlaylistFragment)
                exitWithConfirmation()
        }

        with(binding) {
            backButton.setOnClickListener { exitWithConfirmation() }

            name.editText?.doOnTextChanged { s, _, _, _ -> viewModel.setName(s) }

            description.editText?.doOnTextChanged { s, _, _, _ -> viewModel.setDescription(s) }

            createButton.setOnClickListener { viewModel.onCreateClick() }

            playlistCover.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    }

    private fun exitWithConfirmation() {
        val isFilled = with(binding) {
            val isNameFilled = !name.editText?.text.isNullOrEmpty()
            val isDescriptionFilled = !description.editText?.text.isNullOrEmpty()
            val isCoverFilled = image.tag != null

            isNameFilled or isDescriptionFilled or isCoverFilled
        }

        if (isFilled) confirmDialog.show()
        else exit()
    }

    private fun exit() {
        with(findNavController()) {
            val prevEntry = previousBackStackEntry?.destination?.id
            if (prevEntry == R.id.playerFragment) {
                setFragmentResult(Constants.CREATION_RESULT, bundleOf())
            }
            navigateUp()
        }
    }
}
