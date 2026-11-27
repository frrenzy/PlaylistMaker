package com.example.playlistmaker.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track

@Composable
private fun ArtworkCover(url: String?) {
    val modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(2.dp))
    url?.let { AsyncImage(url, contentDescription = null, modifier = modifier) }
        ?: Image(
            painter = painterResource(R.drawable.ic_placeholder_45),
            contentDescription = null,
            modifier = modifier,
        )
}

@Composable
private fun TrackInfo(track: Track, modifier: Modifier) {
    Column(modifier.background(color = MaterialTheme.colorScheme.surface)) {
        Text(
            text = track.trackName,
            style = TextStyle(
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_size_small).toSp() },
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurface,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val textStyle = TextStyle(
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_size_extra_small).toSp() },
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.secondary,
            )

            Text(text = track.artistName, style = textStyle)
            Text(text = stringResource(R.string.track_info_separator), style = textStyle)
            Text(text = track.trackTime, style = textStyle)
        }
    }
}

@Composable
fun TrackCard(track: Track, onClick: (track: Track) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable(
                enabled = true
            ) { onClick(track) }) {
        ArtworkCover(track.coverArtworkUrl)

        TrackInfo(track, Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward_24),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_button_size))
        )
    }
}

@Preview
@Composable
private fun TrackCardPreview() {
    TrackCard(
        Track(
            trackId = 1L,
            trackName = "Aboba",
            artistName = "The Abobas",
            trackTimeMillis = 100 * 1000,
            artworkUrl100 = null,
            collectionName = "Best of abobas",
            releaseDate = null,
            primaryGenreName = "EEEE rock",
            country = "USA",
            previewUrl = null,
            coverArtworkUrl = null,
            isFavourite = true,
        ), {}
    )
}
