package com.example.playlistmaker.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.ActionButton
import com.example.playlistmaker.common.ui.TrackList
import com.example.playlistmaker.history.presentation.HistoryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchHistory(onTrackClick: (track: Track) -> Unit) {
    val viewModel = koinViewModel<HistoryViewModel>()
    val history by viewModel.history.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedTracks()
    }

    if (history.isVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.search_history_title),
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_section)),
                style = TextStyle(
                    fontWeight = FontWeight(500),
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )

            TrackList(
                tracks = history.tracks,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_section)
                )
            ) { onTrackClick(it) }

            ActionButton(textRes = R.string.search_history_clear) { viewModel.onClearHistory() }
        }
    }
}
