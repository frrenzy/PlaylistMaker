package com.example.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private var playlistsLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Empty)
    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsLiveData

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect {
                val state = if (it.isEmpty()) PlaylistsState.Empty
                else PlaylistsState.Playlists(it)

                playlistsLiveData.postValue(state)
            }
        }
    }
}

sealed interface PlaylistsState {
    data class Playlists(val playlists: List<Playlist>) : PlaylistsState
    object Empty : PlaylistsState
}
