package com.example.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistsViewModel : ViewModel() {
    private var playlistsLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Empty)
    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsLiveData
}

sealed interface PlaylistsState {
    data class Playlists(val playlists: List<Playlist>) : PlaylistsState
    object Empty : PlaylistsState
}
