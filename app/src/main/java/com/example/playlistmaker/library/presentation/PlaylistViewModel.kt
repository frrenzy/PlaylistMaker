package com.example.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private val playlistLiveData = MutableLiveData<Pair<Playlist, List<Track>>>()
    fun observePlaylist(): LiveData<Pair<Playlist, List<Track>>> = playlistLiveData

    fun loadPlaylist() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updatePlaylist()
            }
        }
    }

    fun removeTrackFromPlaylist(trackId: Long?) {
        trackId ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistsInteractor.removeTrackFromPlaylist(trackId, playlistId)
                updatePlaylist()
            }
        }
    }

    fun sharePlaylist() {
        val playlist = playlistLiveData.value?.first ?: return
        val tracks = playlistLiveData.value?.second ?: return
        if (tracks.isEmpty()) return

        sharingInteractor.sharePlaylist(playlist, tracks)
    }

    fun deletePlaylist() {
        val playlist = playlistLiveData.value?.first ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistsInteractor.removePlaylist(playlist)
            }
        }
    }

    private suspend fun updatePlaylist() =
        playlistsInteractor.getPlaylistById(playlistId).collect {
            playlistLiveData.postValue(it)
        }
}
