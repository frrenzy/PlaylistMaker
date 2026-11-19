package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.ui.theme.Theme
import com.example.playlistmaker.player.ui.PlayerFragment

class FavouritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Theme {
                    FavouriteTracks(onTrackClick = {
                        findNavController().navigate(
                            R.id.action_libraryFragment_to_playerFragment,
                            PlayerFragment.createArgs(it)
                        )
                    })
                }
            }
        }
    }
}
