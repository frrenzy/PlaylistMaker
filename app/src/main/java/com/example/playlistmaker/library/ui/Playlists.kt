package com.example.playlistmaker.library.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.common.ui.ActionButton
import com.example.playlistmaker.common.ui.EmptyBlock
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.presentation.PlaylistsState
import com.example.playlistmaker.library.presentation.PlaylistsViewModel
import com.example.playlistmaker.utils.getCoverImageFile
import org.koin.androidx.compose.koinViewModel

@Composable
private fun PlaylistCover(path: String?) {
    val modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp))
    path?.let {
        val context = LocalContext.current

        val imageFile = getCoverImageFile(context, path)

        AsyncImage(
            model = ImageRequest.Builder(context).data(imageFile).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
        ?: Image(
            painter = painterResource(R.drawable.ic_placeholder_45),
            contentDescription = null,
            modifier = modifier,
        )
}

@Composable
private fun PlaylistCard(playlist: Playlist, onClick: (playlistId: Long) -> Unit) {
    val textStyle = TextStyle(
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) { onClick(playlist.id) }) {
        PlaylistCover(path = playlist.coverPath)
        Text(text = playlist.name, style = textStyle)
        Text(
            text = pluralStringResource(
                R.plurals.playlist_track_amount,
                playlist.amount,
                playlist.amount
            ),
            style = textStyle,
        )
    }
}

@Composable
private fun PlaylistList(playlists: List<Playlist>, onPlaylistClick: (playlistId: Long) -> Unit) {
    val gridArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = gridArrangement,
        horizontalArrangement = gridArrangement,
    ) {
        items(playlists) { playlist ->
            PlaylistCard(playlist, onPlaylistClick)
        }
    }
}

@Composable
fun Playlists(onPlaylistClick: (playlistId: Long) -> Unit, onNewPlaylistClick: () -> Unit) {
    val viewModel = koinViewModel<PlaylistsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    val state by viewModel.playlists.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        ActionButton(textRes = R.string.new_playlist, onClick = onNewPlaylistClick)

        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_default)))

        when (state) {
            is PlaylistsState.Empty -> EmptyBlock(
                textRes = R.string.library_playlists_empty,
                modifier = Modifier.padding(top = 38.dp)
            )

            is PlaylistsState.Playlists -> PlaylistList((state as PlaylistsState.Playlists).playlists) {
                onPlaylistClick(it)
            }
        }
    }
}

@Preview
@Composable
private fun PlaylistsPreview() {
    Playlists({}, {})
}
