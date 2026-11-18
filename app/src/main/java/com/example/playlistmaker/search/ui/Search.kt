package com.example.playlistmaker.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.ActionButton
import com.example.playlistmaker.common.ui.EmptyBlock
import com.example.playlistmaker.common.ui.ScreenLayout
import com.example.playlistmaker.common.ui.TrackList
import com.example.playlistmaker.history.presentation.HistoryViewModel
import com.example.playlistmaker.search.presentation.SearchState
import com.example.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(44.dp))
    }
}

@Composable
private fun Content(tracks: List<Track> = emptyList(), onTrackClick: (track: Track) -> Unit) {
    val historyViewModel = koinViewModel<HistoryViewModel>()

    TrackList(tracks = tracks, modifier = Modifier.fillMaxWidth()) {
        historyViewModel.onTrackClick(it)
        onTrackClick(it)
    }
}

@Composable
private fun NetworkErrorBlock(onActionClick: () -> Unit) {
    val textStyle = TextStyle(
        fontWeight = FontWeight(500),
        fontSize = 19.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.ic_network_error_120),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_default)))

        Text(
            text = stringResource(R.string.network_error),
            style = textStyle,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
        )

        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_default)))

        Text(
            text = stringResource(R.string.network_error_hint),
            style = textStyle,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                .padding(bottom = dimensionResource(R.dimen.padding_section))
        )

        ActionButton(textRes = R.string.reload) { onActionClick() }
    }
}


@Composable
fun Search(onTrackClick: (track: Track) -> Unit) {
    val searchViewModel = koinViewModel<SearchViewModel>()
    val searchState by searchViewModel.searchState.collectAsState()
    val searchTerm by searchViewModel.searchTerm.collectAsState()

    val historyViewModel = koinViewModel<HistoryViewModel>()

    LaunchedEffect(Unit) {
        historyViewModel.loadSavedTracks()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ScreenLayout(R.string.search, showBackButton = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_default))
                    .height(52.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                TextField(
                    value = searchTerm,
                    onValueChange = {
                        searchViewModel.onSearchTextChanged(it)
                        historyViewModel.onSearchTextChanged(it)
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.search),
                            style = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_16),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            searchViewModel.onPressEnter()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { historyViewModel.onSearchFocus(it.hasFocus) }
                )
                if (searchTerm.isNotEmpty()) Box(
                    modifier = Modifier
                        .size(height = 36.dp, width = 40.dp)
                        .clickable(enabled = true) {
                            searchViewModel.onClear()
                            historyViewModel.onClearSearch()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_16),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }

            SearchHistory { onTrackClick(it) }

            when (searchState) {
                is SearchState.Default -> Content {}
                is SearchState.Loading -> Loading()
                is SearchState.Error -> NetworkErrorBlock { searchViewModel.onReload() }
                is SearchState.Empty -> EmptyBlock(
                    textRes = R.string.not_found_error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 102.dp)
                )

                is SearchState.Content -> Content((searchState as SearchState.Content).tracks) {
                    onTrackClick(it)
                }
            }
        }
    }
}
