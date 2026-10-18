package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.CreateResult
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.services.MusicPlayerControl
import com.example.playlistmaker.player.services.MusicPlayerState
import com.example.playlistmaker.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    private val track: Track,
    private val favouritesInteractor: FavouriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {
    private var musicPlayerControl: MusicPlayerControl? = null

    private val trackLiveData = MutableLiveData<Track>()
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    private val message = MutableLiveData<Event<AddTrackToPlaylistResult>>()
    fun observeMessage(): LiveData<Event<AddTrackToPlaylistResult>> = message

    fun loadTrack() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouritesInteractor.getTrackIds().collect {
                    val isTrackInFavourite = it.contains(track.trackId)
                    trackLiveData.postValue(track.copy(isFavourite = isTrackInFavourite))
                }
            }
        }
    }

    fun setMusicPlayerControl(control: MusicPlayerControl) {
        musicPlayerControl = control

        viewModelScope.launch {
            musicPlayerControl?.getPlayerState()?.collect {
                playerStateLiveData.postValue(mapState(it))
            }
        }
    }

    fun removeAudioPlayerControl() {
        musicPlayerControl = null
    }

    fun onPlayButtonClick() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> Unit
        }
    }

    fun onLikeButtonClick() {
        val track = trackLiveData.value ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (track.isFavourite) {
                    favouritesInteractor.removeTrack(track)
                    trackLiveData.postValue(track.copy(isFavourite = false))
                } else {
                    favouritesInteractor.addTrack(track)
                    trackLiveData.postValue(track.copy(isFavourite = true))
                }
            }
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect { playlistsLiveData.postValue(it) }
        }
    }

    fun onPlaylistClick(playlist: Playlist) {
        val track = trackLiveData.value ?: return

        viewModelScope.launch {
            playlistsInteractor.addTrackToPlaylist(playlist.id, track).collect {
                val result = when (it) {
                    is CreateResult.AlreadyExists -> AddTrackToPlaylistResult.Error(
                        R.string.track_already_in_playlist,
                        playlist.name
                    )

                    is CreateResult.Success -> AddTrackToPlaylistResult.Success(
                        R.string.add_track_to_playlist_success,
                        playlist.name
                    )
                }

                message.postValue(Event(result))
            }
        }
    }

    private fun startPlayer() {
        musicPlayerControl?.startPlayer()
    }

    private fun pausePlayer() {
        musicPlayerControl?.pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()

        musicPlayerControl = null
    }

    fun onFragmentPaused() {
        musicPlayerControl?.startForeground()
    }

    fun onFragmentResumed() {
        musicPlayerControl?.stopForeground()
    }

    private fun mapState(state: MusicPlayerState) = when (state) {
        is MusicPlayerState.Default -> PlayerState.Default
        is MusicPlayerState.Prepared -> PlayerState.Prepared
        is MusicPlayerState.Paused -> PlayerState.Paused(state.progressTime)
        is MusicPlayerState.Playing -> PlayerState.Playing(state.progressTime)
    }
}

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progressTime: String) {
    object Default : PlayerState(false, Track.DEFAULT_TRACK_TIME)
    object Prepared : PlayerState(true, Track.DEFAULT_TRACK_TIME)
    class Playing(progress: String) : PlayerState(true, progress)
    class Paused(progress: String) : PlayerState(true, progress)
}

sealed class AddTrackToPlaylistResult(val stringId: Int, val arg: String) {
    class Success(r: Int, a: String) : AddTrackToPlaylistResult(r, a)
    class Error(r: Int, a: String) : AddTrackToPlaylistResult(r, a)
}
