package com.example.playlistmaker.library.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.EmptyBlock
import com.example.playlistmaker.common.ui.TrackList
import com.example.playlistmaker.library.presentation.FavouritesState
import com.example.playlistmaker.library.presentation.FavouritesViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavouriteTracks(onTrackClick: (track: Track) -> Unit) {
    val viewModel = koinViewModel<FavouritesViewModel>()

    LaunchedEffect(Unit) {
        viewModel.loadTracks()
    }

    val state by viewModel.favourites.collectAsState()

    when (state) {
        is FavouritesState.Empty -> EmptyBlock(
            textRes = R.string.library_favourites_empty,
            modifier = Modifier.padding(top = 106.dp)
        )

        is FavouritesState.Tracks -> TrackList(
            (state as FavouritesState.Tracks).tracks
        ) {
            onTrackClick(it)
        }
    }
}
