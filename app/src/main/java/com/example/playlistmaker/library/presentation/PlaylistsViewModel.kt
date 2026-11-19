package com.example.playlistmaker.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private val _playlists = MutableStateFlow<PlaylistsState>(PlaylistsState.Empty)
    val playlists = _playlists.asStateFlow()

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect {
                val state = if (it.isEmpty()) PlaylistsState.Empty
                else PlaylistsState.Playlists(it)

                _playlists.value = state
            }
        }
    }
}

sealed interface PlaylistsState {
    data class Playlists(val playlists: List<Playlist>) : PlaylistsState
    object Empty : PlaylistsState
}
