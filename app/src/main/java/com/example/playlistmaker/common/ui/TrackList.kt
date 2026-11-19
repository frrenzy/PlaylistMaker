package com.example.playlistmaker.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track

@Composable
fun TrackList(
    tracks: List<Track>,
    modifier: Modifier = Modifier,
    onTrackClick: (track: Track) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_default)),
        modifier = modifier
    ) {
        items(tracks) {
            TrackCard(it, onTrackClick)
        }
    }
}
